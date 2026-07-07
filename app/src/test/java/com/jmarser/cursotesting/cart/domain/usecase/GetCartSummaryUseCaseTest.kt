package com.jmarser.cursotesting.cart.domain.usecase

import com.jmarser.cursotesting.cart.data.repository.FakeCartRepositoryImpl
import com.jmarser.cursotesting.cart.data.repository.FakeProductRepositoryImpl
import com.jmarser.cursotesting.core.builders.cartItem
import com.jmarser.cursotesting.core.builders.product
import com.jmarser.cursotesting.core.builders.promotion
import com.jmarser.cursotesting.core.data.util.FakeClock
import com.jmarser.cursotesting.productlist.data.repository.FakePromotionRepository
import com.jmarser.cursotesting.productlist.domain.model.PromotionType
import com.jmarser.cursotesting.productlist.domain.usecase.GetPromotionForProduct
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.Instant

class GetCartSummaryUseCaseTest {

    private lateinit var clock: FakeClock
    private lateinit var cartRepository: FakeCartRepositoryImpl
    private lateinit var productRepository: FakeProductRepositoryImpl
    private lateinit var promoRepository: FakePromotionRepository


    @Before
    fun setUp() {
        clock = FakeClock().apply { setTime(Instant.parse("2026-05-08T10:00:00Z")) }
        cartRepository = FakeCartRepositoryImpl()
        productRepository = FakeProductRepositoryImpl()
        promoRepository = FakePromotionRepository()
    }

    private fun useCase() = GetCartSummaryUseCase(
        cartRepository,
        productRepository,
        promoRepository,
        GetPromotionForProduct(),
        clock
    )

    @Test
    fun `given percent promotion when invoke then calculate correctly`() = runTest {
        val productId = "P1"
        val product = product {
            withId(productId)
            withPrice(100.0)
        }
        val promo = promotion {
            withProductIds(listOf(productId))
            withType(PromotionType.PERCENT)
            withValue(10.0)
            withStartTime(clock.now().minusSeconds(10))
            withEndTime(clock.now().plusSeconds(10))
        }

        val carItem = cartItem {
            withProductId(productId)
            withQuantity(2)
        }

        productRepository.setProducts(listOf(product))
        promoRepository.setPromotions(listOf(promo))
        cartRepository.setCartItems(listOf(carItem))

        val result = (useCase()()).first()

        assertEquals(180.0, result.finalTotal)
        assertEquals(20.0, result.discountTotal)
        assertEquals(200.0, result.subTotal)
    }

    @Test
    fun `given 3 items in 2x1 promotion when invoke then only discount 1 unit`() = runTest {
        val productId = "P1"
        val product = product {
            withId(productId)
            withPrice(100.0)
        }
        val promo = promotion {
            withProductIds(listOf(productId))
            withType(PromotionType.BUY_X_PAY_Y)
            withBuyQuantity(2)
            withValue(1.0)
            withStartTime(clock.now().minusSeconds(10))
            withEndTime(clock.now().plusSeconds(10))
        }

        val carItem = cartItem {
            withProductId(productId)
            withQuantity(3)
        }

        productRepository.setProducts(listOf(product))
        promoRepository.setPromotions(listOf(promo))
        cartRepository.setCartItems(listOf(carItem))

        val result = (useCase()()).first()

        assertEquals(300.0, result.subTotal)
        assertEquals(200.0, result.finalTotal)
        assertEquals(100.0, result.discountTotal)
    }

    @Test
    fun `given multiple products with different promotions when invoke them sums all correctly`() =
        runTest {
            val now = clock.now()
            val product1Id = "P1"
            val product2Id = "P2"
            val product1 = product {
                withId(product1Id)
                withPrice(100.0)
            }
            val product2 = product {
                withId(product2Id)
                withPrice(50.0)
            }

            val promoPercent = promotion {
                withProductIds(listOf(product1Id))
                withType(PromotionType.PERCENT)
                withValue(10.0)
                withStartTime(clock.now().minusSeconds(10))
                withEndTime(clock.now().plusSeconds(10))
            }

            val cart = listOf(
                cartItem {
                    withProductId(product1Id)
                    withQuantity(1)
                },
                cartItem {
                    withProductId(product2Id)
                    withQuantity(1)
                }
            )

            productRepository.setProducts(listOf(product1, product2))
            promoRepository.setPromotions(listOf(promoPercent))
            cartRepository.setCartItems(cart)

            val result = useCase()().first()

            assertEquals(150.0, result.subTotal)
            assertEquals(140.0, result.finalTotal)
            assertEquals(10.0, result.discountTotal)
        }

    @Test
    fun `given expired promotion when invoke then discount is zero`() = runTest {
        val now = clock.now()
        val productId = "id1"
        val product = product {
            withId(productId)
            withPrice(100.0)
        }

        val promoExpired = promotion {
            withProductIds(listOf(productId))
            withType(PromotionType.PERCENT)
            withValue(10.0)
            withStartTime(now.minusSeconds(10))
            withEndTime(now.minusSeconds(5))
        }

        val cart = cartItem {
            withProductId(productId)
            withQuantity(1)
        }

        productRepository.setProducts(listOf(product))
        promoRepository.setPromotions(listOf(promoExpired))
        cartRepository.setCartItems(listOf(cart))

        val result = useCase()().first()

        assertEquals(0.0, result.discountTotal)
        assertEquals(100.0, result.finalTotal)
    }

    @Test
    fun `given active promotion when time advances then summary update automatically`() = runTest {
        val now = clock.now()
        val productId = "id1"
        val product = product {
            withId(productId)
            withPrice(100.0)
        }

        val promoExpired = promotion {
            withProductIds(listOf(productId))
            withType(PromotionType.PERCENT)
            withValue(10.0)
            withStartTime(now.minusSeconds(10))
            withEndTime(now.plusSeconds(5))
        }

        val cart = cartItem {
            withProductId(productId)
            withQuantity(1)
        }

        productRepository.setProducts(listOf(product))
        promoRepository.setPromotions(listOf(promoExpired))
        cartRepository.setCartItems(listOf(cart))

        val result = useCase()()

        assertEquals(10.0, result.first().discountTotal)
        clock.advanceTime(6)
        assertEquals(0.0, result.first().discountTotal)
    }

    @Test
    fun `given empty cart when invoke then return zero summary`() = runTest {
        cartRepository.setCartItems(emptyList())

        val result = useCase()().first()

        assertEquals(0.0, result.subTotal)
        assertEquals(0.0, result.finalTotal)
    }

    @Test
    fun `given product in cart does not exist in repository when invoke then ignore it`() =
        runTest {
            val cart = listOf(cartItem { withProductId("ProductId1"); withQuantity(1) })

            cartRepository.setCartItems(cart)

            val result = useCase()().first()

            assertEquals(0.0, result.subTotal)
        }

    @Test
    fun `given BuyXPayY promotion and not enough items when invoke then discount is zero`() =
        runTest {
            val productId = "productId1"
            val product = product {
                withId(productId)
                withPrice(100.0)
            }
            val promo = promotion {
                withProductIds(listOf(productId))
                withType(PromotionType.BUY_X_PAY_Y)
                withBuyQuantity(3)
                withValue(1.0)
                withStartTime(clock.now().minusSeconds(10))
                withEndTime(clock.now().plusSeconds(10))
            }

            val cartItem = cartItem {
                withProductId(productId)
                withQuantity(2)
            }

            val cartItems = listOf(cartItem)

            productRepository.setProducts(listOf(product))
            promoRepository.setPromotions(listOf(promo))
            cartRepository.setCartItems(cartItems)

            val result = useCase()().first()

            assertEquals(200.0, result.subTotal)
            assertEquals(0.0, result.discountTotal)
        }

    @Test
    fun `given mixed promotions when invoke then calculates sum of all discounts correctly`() =
        runTest {
            val productId1 = "pizza" //promo 2x1 con un precio de 10
            val productId2 = "cola" //  promo 10% descuento
            val productPizza = product {
                withId(productId1)
                withPrice(10.0)
            }
            val productCola = product {
                withId(productId2)
                withPrice(2.0)
            }

            productRepository.setProducts(listOf(productPizza, productCola))

            val promo2X1 = promotion {
                withProductIds(listOf(productId1))
                withType(PromotionType.BUY_X_PAY_Y)
                withBuyQuantity(2)
                withValue(1.0)
                withStartTime(clock.now().minusSeconds(10))
                withEndTime(clock.now().plusSeconds(10))
            }

            val promoPercent = promotion {
                withProductIds(listOf(productId2))
                withType(PromotionType.PERCENT)
                withValue(10.0)
                withStartTime(clock.now().minusSeconds(10))
                withEndTime(clock.now().plusSeconds(10))
            }

            promoRepository.setPromotions(listOf(promo2X1, promoPercent))

            cartRepository.setCartItems(
                listOf(
                cartItem {
                    withProductId(productId1)
                    withQuantity(3)
                },
                cartItem {
                    withProductId(productId2)
                    withQuantity(2)
                }
            ))

            val result = useCase()().first()

            assertEquals(34.0, result.subTotal)
            assertEquals(10.4, result.discountTotal) // 10€/pizza - 10%/cola
            assertEquals(23.6, result.finalTotal)
        }
}