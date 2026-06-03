package com.jmarser.cursotesting.productlist.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jmarser.cursotesting.core.mockwebserver.MockWebServerUrlHolder
import com.jmarser.cursotesting.core.mockwebserver.rules.MockWebServerRule
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ProductRepositoryImplTest {


    @get:Rule(order = 0)
    val mockWebServer = MockWebServerRule()

    @get:Rule(order = 1)
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var productRepository: ProductRepository

    @Before
    fun setup() {
        hilt.inject()
    }

    @After
    fun tearDown() {
        MockWebServerUrlHolder.baseUrl = "http://localhost:8080/"
    }

    private val productsJson = """
        {"products":[
            {"id":"p1","name":"pan","description":"pan fresco","priceCents":150,"category":"Comida","stock":10},
            {"id":"p2","name":"leche","description":"Leche entera","priceCents":200,"category":"Lacteos","stock":5}
        ]}
    """.trimIndent()


    @Test
    fun given_validProductsJson_when_refresh_isCalled_then_database_emit_products_from_room() =
        runTest {
            mockWebServer.server.enqueue(MockResponse().setBody(productsJson).setResponseCode(200))
            productRepository.refreshProduct()

            val products = productRepository.getProducts().first()

            assertTrue(products.isNotEmpty())
            assertTrue(products.size == 2)
            assertEquals("pan", products.find { it.id == "p1" }?.name)

        }

    @Test
    fun given_emptyProducts_json_when_refresh_isCalled_then_getProducts_emits_emptyList() = runTest {
        mockWebServer.server.enqueue(MockResponse().setBody("""{"products":[]}""").setResponseCode(200))
        productRepository.refreshProduct()

        val products = productRepository.getProducts().first()

        assertTrue(products.isEmpty())
    }

    @Test
    fun given_products_json_when_refresh_and_getProductById_then_returns_correct_product() = runTest {
        mockWebServer.server.enqueue(MockResponse().setBody(productsJson).setResponseCode(200))
        productRepository.refreshProduct()

        val product = productRepository.getProductById("p1").first()

        assertNotNull(product)
        assertEquals("pan", product?.name)

    }

    @Test(expected = Exception::class)
    fun given_server_returns_500_when_refresh_isCalled_then_it_throws_exception() = runTest {

        mockWebServer.server.enqueue(MockResponse().setResponseCode(500))

        productRepository.refreshProduct()
    }

    @Test
    fun given_cached_products_when_refresh_with_new_products_then_flow_emits_updated_data() = runTest {
        mockWebServer.server.enqueue(MockResponse().setBody(productsJson).setResponseCode(200))
        productRepository.refreshProduct()

        val productsJsonUpdated = """
        {"products":[
            {"id":"p1","name":"pan integral","description":"pan fresco","priceCents":450,"category":"Comida","stock":10}
        ]}
    """.trimIndent()

        mockWebServer.server.enqueue(MockResponse().setBody(productsJsonUpdated).setResponseCode(200))
        productRepository.refreshProduct()

        val products = productRepository.getProducts().first()

        assertEquals("pan integral",products.find { it.id == "p1" }?.name)
        assertEquals(4.5,products.find { it.id == "p1" }?.price)
    }

    @Test
    fun given_products_endpoint_when_refresh_isCalled_then_request_is_get_to_correct_path() = runTest {
        mockWebServer.server.enqueue(MockResponse().setBody(productsJson).setResponseCode(200))
        productRepository.refreshProduct()

        val request = mockWebServer.server.takeRequest()

        assertEquals("GET", request.method)
        assertTrue(request.path?.contains("data/products.json") == true)
    }

}