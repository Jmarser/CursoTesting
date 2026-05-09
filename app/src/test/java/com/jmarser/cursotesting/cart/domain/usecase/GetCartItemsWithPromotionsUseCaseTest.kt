package com.jmarser.cursotesting.cart.domain.usecase

import com.jmarser.cursotesting.cart.data.repository.FakeCartRepositoryImpl
import com.jmarser.cursotesting.cart.data.repository.FakeProductRepositoryImpl
import com.jmarser.cursotesting.core.builders.cartItem
import com.jmarser.cursotesting.core.builders.product
import com.jmarser.cursotesting.core.builders.promotion
import com.jmarser.cursotesting.core.data.util.FakeClock
import com.jmarser.cursotesting.productlist.data.repository.FakePromotionRepository
import com.jmarser.cursotesting.productlist.domain.usecase.GetPromotionForProduct
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.time.Instant

class GetCartItemsWithPromotionsUseCaseTest {

    private val clock = FakeClock().apply { setTime(Instant.parse("2026-04-03T10:00:00Z")) }

    private fun useCase(
        cart: FakeCartRepositoryImpl = FakeCartRepositoryImpl(),
        products: FakeProductRepositoryImpl = FakeProductRepositoryImpl(),
        promos: FakePromotionRepository = FakePromotionRepository(),
        clock: FakeClock = this.clock
    ) = GetCartItemsWithPromotionsUseCase(
        cart,
        products,
        promos,
        GetPromotionForProduct(),
        clock
    )

    @Test
    fun `given empty cart when invokes then returns empty list`() = runTest {
        val cart = FakeCartRepositoryImpl().apply { setCartItems(emptyList()) }
        val result = (useCase(cart = cart)()).first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given existing cart item with active promotion when invoke`() = runTest {
        val productId = "productId"
        val produt = product {
            withId(productId)
        }

        val now = clock.now()

        val promo = promotion {
            withProductIds(listOf(productId))
            withStartTime(now.minusSeconds(10))
            withEndTime(now.plusSeconds(10))
        }

        val cartItem = cartItem {
            withProductId(productId)
            withQuantity(2)
        }

        val cart = FakeCartRepositoryImpl().apply { setCartItems(listOf(cartItem)) }
        val products = FakeProductRepositoryImpl().apply { setProducts(listOf(produt)) }
        val promotions = FakePromotionRepository().apply { setPromotions(listOf(promo)) }

        val result = useCase(cart = cart, products, promotions)().first()

        assertEquals(1, result.size)
        assertNotNull(result.first().item.promotion)
    }

    @Test
    fun `given cartItem without matching product when invoke then skip intem`() = runTest {
        val cart = FakeCartRepositoryImpl().apply {
            setCartItems(listOf(cartItem { withProductId("ghostId") }))
        }
        val products = FakeProductRepositoryImpl().apply { setProducts(listOf(product { withId("otherId") })) }

        val result = useCase(cart = cart, products = products)().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given promotion ending exactly now when invoke then it must be include`() = runTest {
        val now = clock.now()
        val productId = "productId"

        val product = product { withId(productId) }
        val endingPromotion = promotion {
            withProductIds(listOf(productId))
            withStartTime(now.minusSeconds(100))
            withEndTime(now)
        }

        val cartItem = cartItem {
            withProductId(productId)
        }

        val cart = FakeCartRepositoryImpl().apply { setCartItems(listOf(cartItem)) }
        val products = FakeProductRepositoryImpl().apply { setProducts(listOf(product)) }
        val promotions = FakePromotionRepository().apply { setPromotions(listOf(endingPromotion)) }

        val result = useCase(cart = cart, products = products, promos = promotions)().first()

        assertNotNull(result.first().item.promotion)
    }

    @Test
    fun `given expired promotion when invoke then item remains but without promotion`() = runTest {
        val now = clock.now()
        val productId = "productId"

        val product = product { withId(productId) }
        val endPromotion = promotion {
            withProductIds(listOf(productId))
            withStartTime(now.minusSeconds(100))
            withEndTime(now.minusSeconds(1))
        }

        val cartItem = cartItem {
            withProductId(productId)
        }

        val cart = FakeCartRepositoryImpl().apply { setCartItems(listOf(cartItem)) }
        val products = FakeProductRepositoryImpl().apply { setProducts(listOf(product)) }
        val promotions = FakePromotionRepository().apply { setPromotions(listOf(endPromotion)) }

        val result = useCase(cart = cart, products = products, promos = promotions)().first()

        assertNull(result.first().item.promotion)
    }

    @Test
    fun `given active promotion when time advances then flow emits update list without promotion`() = runTest {
        val now = clock.now()
        val productId = "productId"

        val product = product { withId(productId) }
        val promotion = promotion {
            withProductIds(listOf(productId))
            withStartTime(now.minusSeconds(100))
            withEndTime(now.plusSeconds(5))
        }

        val cartItem = cartItem {
            withProductId(productId)
        }

        val cart = FakeCartRepositoryImpl().apply { setCartItems(listOf(cartItem)) }
        val products = FakeProductRepositoryImpl().apply { setProducts(listOf(product)) }
        val promotions = FakePromotionRepository().apply { setPromotions(listOf(promotion)) }

        val myUseCase =useCase(cart = cart, products = products, promos = promotions)()
        val firstEmission = myUseCase.first()
        assertNotNull(firstEmission.first().item.promotion)

        clock.advanceTime(6)
        val secondEmission = myUseCase.first()
        assertNull(secondEmission.first().item.promotion)
    }
}