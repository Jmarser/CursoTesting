package com.jmarser.cursotesting.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jmarser.cursotesting.core.data.local.database.MarketDatabase
import com.jmarser.cursotesting.core.domain.model.AppError
import com.jmarser.cursotesting.core.mockwebserver.MarketApiDispatcher
import com.jmarser.cursotesting.core.mockwebserver.MockWebServerUrlHolder
import com.jmarser.cursotesting.core.mockwebserver.ProductErrorDispatcher
import com.jmarser.cursotesting.core.mockwebserver.rules.MockWebServerRule
import com.jmarser.cursotesting.core.utils.asAsset
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Project: CursoTesting
 * File: OfflineFirstIntegrationTest.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 06/06/2026
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class OfflineFirstIntegrationTest {

    companion object{
        const val DEFAULT_PRODUCTS_ASSET = "product_list_default.json"
        const val DEFAULT_PRODUCTS_SIZE = 3
        const val UPDATED_PRODUCTS_ASSET = "product_list_updated.json"
        const val UPDATED_PRODUCTS_SIZE = 5
    }

    @get:Rule(order = 0)
    val mockWebServer = MockWebServerRule()

    @get:Rule(order = 1)
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var db: MarketDatabase

    @Inject
    lateinit var productRepository: ProductRepository

    @Before
    fun setUp(){
        hilt.inject()
        db.clearAllTables()
    }

    @After
    fun tearDown(){
        MockWebServerUrlHolder.baseUrl = "http://localhost:8080/"
    }

    @Test
    fun given_success_fullRefresh_when_getProducts_then_room_contains_remote_products() = runTest {
        serveProductsFromAsset(DEFAULT_PRODUCTS_ASSET)

        productRepository.refreshProduct()

        val cachedProducts = productRepository.getProducts().first {
            products -> products.size == DEFAULT_PRODUCTS_SIZE
        }

        assertEquals(DEFAULT_PRODUCTS_SIZE, cachedProducts.size)
    }

    @Test
    fun given_emptyCache_and_faild_refresh_when_getProducts_then_emits_emptyList() = runTest {
        serveProductError()

        assertFailsWith<AppError.NetworkError>{
            productRepository.refreshProduct()
        }

        val products =productRepository.getProducts().first { it.isEmpty() }

        assertTrue(products.isEmpty())
    }

    @Test
    fun given_cachedProducts_and_failed_refresh_when_getProducts_then_returns_previusCache() = runTest {
        serveProductsFromAsset(DEFAULT_PRODUCTS_ASSET)

        productRepository.refreshProduct()

        productRepository.getProducts().first {
                products -> products.size == DEFAULT_PRODUCTS_SIZE
        }

        serveProductError()
        assertFailsWith<AppError.NetworkError>{
            productRepository.refreshProduct()
        }

        val result = productRepository.getProducts().first{products ->
            products.size == DEFAULT_PRODUCTS_SIZE
        }

        assertEquals(DEFAULT_PRODUCTS_SIZE, result.size)
    }

    @Test
    fun given_cachedProducts_when_refresh_with_new_payload_then_contains_only_latest_products() = runTest {
        serveProductsFromAsset(DEFAULT_PRODUCTS_ASSET)
        productRepository.refreshProduct()
        productRepository.getProducts().first {
                products -> products.size == DEFAULT_PRODUCTS_SIZE
        }

        serveProductsFromAsset(UPDATED_PRODUCTS_ASSET)
        productRepository.refreshProduct()

        val result = productRepository.getProducts().first {
                products -> products.size == UPDATED_PRODUCTS_SIZE
        }

        assertEquals(UPDATED_PRODUCTS_SIZE, result.size)
    }


    private fun serveProductsFromAsset(assetName: String){
        mockWebServer.server.dispatcher = MarketApiDispatcher(productJson = assetName.asAsset())
    }

    private fun serveProductError(){
        mockWebServer.server.dispatcher = ProductErrorDispatcher()
    }
}