package com.jmarser.cursotesting.productlist.domain.usecase

import com.jmarser.cursotesting.cart.data.repository.FakeProductRepositoryImpl
import com.jmarser.cursotesting.core.builders.product
import com.jmarser.cursotesting.core.builders.promotion
import com.jmarser.cursotesting.core.data.util.FakeClock
import com.jmarser.cursotesting.productlist.data.repository.FakePromotionRepository
import com.jmarser.cursotesting.productlist.data.repository.FakeSettingsRepository
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Instant

class GetProductsUseCaseTest {

    private fun useCase(
        products: FakeProductRepositoryImpl = FakeProductRepositoryImpl(),
        promos: FakePromotionRepository = FakePromotionRepository(),
        settings: FakeSettingsRepository = FakeSettingsRepository(),
        clock: FakeClock = FakeClock()
    ) = GetProductsUseCase(products, promos, GetPromotionForProduct(), settings, clock)

    @Test
    fun `given promotion ending now when invoke then it should be include`() = runTest {
        // GIVEN
        val now = Instant.parse("2026-05-03T10:00:00Z")
        val clock = FakeClock().apply { setTime(now) }

        val productId = "product-id"
        val produt = product {
            withId(productId)
        }

        val promo = promotion {
            withProductIds(listOf(productId))
            withStartTime(now.minusSeconds(60))
            withEndTime(now)
        }

        val productRepository = FakeProductRepositoryImpl().apply { setProducts(listOf(produt)) }
        val promoRepository = FakePromotionRepository().apply { setPromotions(listOf(promo)) }

        // WHEN
        val result = (useCase(products = productRepository, promos = promoRepository, clock = clock)()).first()

        // THEN
        assertNotNull(result.first())
    }

    @Test
    fun `given active promotion when time advances then promotion should no be longer be returned`() = runTest {
        // GIVEN
        val now = Instant.parse("2026-05-03T10:00:00Z")
        val clock = FakeClock().apply { setTime(now) }

        val productId = "product-id"
        val produt = product {
            withId(productId)
        }

        val promo = promotion {
            withProductIds(listOf(productId))
            withStartTime(now)
            withEndTime(now.plusSeconds(5))
        }

        val productRepository = FakeProductRepositoryImpl().apply { setProducts(listOf(produt)) }
        val promoRepository = FakePromotionRepository().apply { setPromotions(listOf(promo)) }

        // WHEN
        val firstResult = (useCase(products = productRepository, promos = promoRepository, clock = clock)()).first()

        clock.advanceTime(6)
        val secondResult = (useCase(products = productRepository, promos = promoRepository, clock = clock)()).first()

        // THEN
        assertNotNull(firstResult.first().promotion)
        assertNull(secondResult.first().promotion)

    }

    @Test
    fun `given inStockOnly enabled when product goes out ot stock then it should be filtered`() = runTest {
        // GIVEN
        val productId = "product-id"
        val produt = product {
            withId(productId)
            withStock(0)
        }

        val settings = FakeSettingsRepository().apply { setInStockOnly(true) }
        val product = FakeProductRepositoryImpl().apply { setProducts(listOf(produt)) }

        val myUseCase = useCase(settings = settings, products = product)

        // WHEN
        val result = myUseCase().first()


        // THEN
        assertTrue(result.isEmpty())
    }

}

// GIVEN

// WHEN

// THEN