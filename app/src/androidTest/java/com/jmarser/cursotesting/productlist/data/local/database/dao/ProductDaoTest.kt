package com.jmarser.cursotesting.productlist.data.local.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.jmarser.cursotesting.core.builder.productEntity
import com.jmarser.cursotesting.core.data.local.database.MarketDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductDaoTest {

    private lateinit var dataBase: MarketDatabase
    private lateinit var dao: ProductDao

    @Before
    fun setUp() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MarketDatabase::class.java
        ).build()

        dao = dataBase.productDao()
    }

    @After
    fun tearDown() {
        dataBase.close()
    }

    @Test
    fun given_empty_database_when_get_all_products_then_emits_empty_list() = runTest {
        val products = dao.getAllProducts().first()

        assertTrue(products.isEmpty())
    }

    @Test
    fun given_insert_product_when_getProductById_then_returns_row() = runTest {
        val productId = "p1"
        val productEntity = productEntity {
            withId(productId)
        }
        dao.insertAllProducts(listOf(productEntity))

        val product = dao.getProductById(productId).first()

        assertNotNull(product)
        assertEquals(productId, product.id)
    }

    @Test
    fun given_three_products_when_getProductsByIds_then_returns_request_subset() = runTest {
        dao.insertAllProducts(
            listOf(
                productEntity { withId("1") },
                productEntity { withId("2") },
                productEntity { withId("3") }
            ))

        val products = dao.getProductsIds(listOf("1", "3")).first()

        assertTrue(products.any { it.id == "1" })
        assertTrue(products.any { it.id == "3" })
        assertTrue(products.none { it.id == "2" })
    }

    @Test
    fun given_old_products_when_replaceAll_then_only_new_products_remain() = runTest {
        dao.insertAllProducts(
            listOf(
                productEntity { withId("old-1") },
                productEntity { withId("old-2") }
            ))

        val newProducts = listOf(
            productEntity { withId("new-1") },
            productEntity { withId("new-2") },
            productEntity { withId("new-3") }
        )

        dao.replaceAll(newProducts)

        val result = dao.getAllProducts().first()

        assertEquals(3, result.size)
        assertTrue(result.none {
            it.id == "old-1" || it.id == "old-2"
        })
        assertTrue(result.any { it.id == "new-1" } && result.any { it.id == "new-2" } && result.any { it.id == "new-3" })
    }

    @Test
    fun given_existing_product_when_insert_same_id_with_different_data_then_replace_old_data() = runTest {
        val productId = "p1"
        val productEntity1 = productEntity {
            withId(productId)
            withName("Pan")
        }
        dao.insertAllProducts(listOf(productEntity1))

        val productEntity2 = productEntity {
            withId(productId)
            withName("Leche")
        }
        dao.insertAllProducts(listOf(productEntity2))

        val result = dao.getAllProducts().first()

        assertTrue(result.size == 1)
        assertEquals("Leche", result.first().name)
    }

    @Test
    fun given_flow_subscribed_when_insert_after_subscribe_then_emits_updateList() = runTest {
        dao.getAllProducts().test {
            val initial = awaitItem()

            dao.insertAllProducts(listOf(productEntity { withId("1") }))

            val update = awaitItem()

            assertTrue(initial.isEmpty())
            assertEquals(1, update.size)
            assertEquals("1", update.first().id)
        }
    }
}