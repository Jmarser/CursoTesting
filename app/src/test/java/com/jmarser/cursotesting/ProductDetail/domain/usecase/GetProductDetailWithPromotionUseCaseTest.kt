package com.jmarser.cursotesting.ProductDetail.domain.usecase

import com.jmarser.cursotesting.cart.data.repository.FakeProductRepositoryImpl
import com.jmarser.cursotesting.core.builders.product
import com.jmarser.cursotesting.core.builders.promotion
import com.jmarser.cursotesting.core.data.util.FakeClock
import com.jmarser.cursotesting.productlist.data.repository.FakePromotionRepository
import com.jmarser.cursotesting.productlist.domain.model.ProductPromotion
import com.jmarser.cursotesting.productlist.domain.model.PromotionType
import com.jmarser.cursotesting.productlist.domain.usecase.GetPromotionForProduct
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.experimental.theories.suppliers.TestedOn
import java.time.Instant

class GetProductDetailWithPromotionUseCaseTest {

    private lateinit var clock: FakeClock
    private lateinit var productRepository: FakeProductRepositoryImpl
    private lateinit var promoRepository: FakePromotionRepository

    @Before
    fun setup() {
        clock = FakeClock().apply { setTime(Instant.parse("2026-05-08T10:00:00Z")) }
        productRepository = FakeProductRepositoryImpl()
        promoRepository = FakePromotionRepository()
    }

    private fun useCase() = GetProductDetailWithPromotionUseCase(
        productRepository,
        promoRepository,
        GetPromotionForProduct(),
        clock
    )

    @Test
    fun `given product and active promotion when invoke then return product with promotion`() =
        runTest {
            val productId = "productId1"
            val product = product {
                withId(productId)
                withPrice(10.0)
            }
            val promo = promotion {
                withProductIds(listOf(productId))
                withType(PromotionType.PERCENT)
                withValue(10.0)
                withStartTime(clock.now().minusSeconds(6))
                withEndTime(clock.now().plusSeconds(6))
            }
            productRepository.setProducts(listOf(product))
            promoRepository.setPromotions(listOf(promo))

            val result = useCase()(productId).first()

            assertNotNull(result)
            assertEquals(productId, result?.product?.id)
            assertTrue(result?.promotion is ProductPromotion.Percent)
        }

    @Test
    fun `given product does not exist when invoke then emit null`() = runTest {
        productRepository.setProducts(emptyList())

        val result = useCase()("Non_existent").first()

        assertNull(result)
    }

    @Test
    fun `given product with expired promotion when invoke then return product with null promotion`() = runTest {
        val productId = "productId"
        val product = product {
            withId(productId)
        }

        val expìredPromo = promotion {
            withProductIds(listOf(productId))
            withStartTime(clock.now().minusSeconds(20))
            withEndTime(clock.now().minusSeconds(10))
        }

        productRepository.setProducts(listOf(product))
        promoRepository.setPromotions(listOf(expìredPromo))

        val result = useCase()(productId).first()

        assertNotNull(result)
        assertNull(result?.promotion)
    }
}