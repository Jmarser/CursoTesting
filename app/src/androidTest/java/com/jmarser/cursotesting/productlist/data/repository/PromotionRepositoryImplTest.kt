package com.jmarser.cursotesting.productlist.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jmarser.cursotesting.core.mockwebserver.MockWebServerUrlHolder
import com.jmarser.cursotesting.core.mockwebserver.rules.MockWebServerRule
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
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
class PromotionRepositoryImplTest {

    @get:Rule(order = 0)
    val mockWebServer = MockWebServerRule()

    @get:Rule(order = 1)
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var promotionRepository: PromotionRepository

    @Before
    fun setUp() {
        hilt.inject()
    }

    @After
    fun tearDown() {
        MockWebServerUrlHolder.baseUrl = "http://localhost:8080/"
    }

    private fun readJson(fileName: String): String{
        val context = InstrumentationRegistry.getInstrumentation().context
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    @Test
    fun given_active_promotions_json_when_refresh_isCalled_then_flow_emits_active_promotions() =
        runTest {
            val json = readJson("promotions_percent.json")
            mockWebServer.server.enqueue(MockResponse().setBody(json).setResponseCode(200))

            promotionRepository.refreshPromotions()

            val promotions = promotionRepository.getActivePromotions().first()

            assertTrue(promotions.isNotEmpty())
        }

    @Test
    fun given_empty_promotions_json_when_refresh_isCalled_then_list_is_empty() = runTest {
        val json = readJson("promotions_empty.json")
        mockWebServer.server.enqueue(MockResponse().setBody(json).setResponseCode(200))

        promotionRepository.refreshPromotions()

        val promotions = promotionRepository.getActivePromotions().first()

        assertTrue(promotions.isEmpty())
    }

    @Test
    fun given_buyX_payY_json_when_refresh_isCalled_then_domain_maps_quantities_correctly() = runTest {
        val json = readJson("promotions_buy_x_pay_y.json")
        mockWebServer.server.enqueue(MockResponse().setBody(json).setResponseCode(200))

        promotionRepository.refreshPromotions()
        val promotion = promotionRepository.getActivePromotions().first().find { it.id == "promo1" }

        assertNotNull(promotion)
        assertEquals(2.0, promotion?.value)
        assertEquals(3, promotion?.buyQuantity)
    }

    @Test(expected = Exception::class)
    fun given_server_returns_500_when_refresh_isCalled_then_it_throws_exception() = runTest {
        mockWebServer.server.enqueue(MockResponse().setResponseCode(500))

        promotionRepository.refreshPromotions()
    }

    @Test
    fun given_promotions_endpoint_when_refresh_isCalled_then_request_is_get_to_correct_path() = runTest {
        val json = readJson("promotions_percent.json")
        mockWebServer.server.enqueue(MockResponse().setBody(json).setResponseCode(200))

        promotionRepository.refreshPromotions()

        val request = mockWebServer.server.takeRequest()

        assertEquals("GET", request.method)
        assertTrue(request.path?.contains("data/promotions.json") == true)

    }
}