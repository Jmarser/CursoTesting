package com.jmarser.cursotesting.cart.presentation

import app.cash.turbine.test
import com.jmarser.cursotesting.cart.data.repository.FakeCartRepositoryImpl
import com.jmarser.cursotesting.cart.data.repository.FakeProductRepositoryImpl
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.cart.domain.usecase.GetCartItemsWithPromotionsUseCase
import com.jmarser.cursotesting.cart.domain.usecase.GetCartSummaryUseCase
import com.jmarser.cursotesting.cart.domain.usecase.UpdateCartItemUseCase
import com.jmarser.cursotesting.core.builders.cartItem
import com.jmarser.cursotesting.core.builders.product
import com.jmarser.cursotesting.core.data.util.FakeClock
import com.jmarser.cursotesting.core.domain.util.Clock
import com.jmarser.cursotesting.core.utils.MainDispatcherRule
import com.jmarser.cursotesting.productlist.data.repository.FakePromotionRepository
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
import com.jmarser.cursotesting.productlist.domain.usecase.GetPromotionForProduct
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CartViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        cartRepository: CartRepository = FakeCartRepositoryImpl(),
        productRepository: ProductRepository = FakeProductRepositoryImpl(),
        promotionRepository: PromotionRepository = FakePromotionRepository(),
        clock: Clock = FakeClock()
    ): CartViewModel{
        val getCarrtSummaryUseCase = GetCartSummaryUseCase(
            cartRepository,
            productRepository,
            promotionRepository,
            GetPromotionForProduct(),
            clock
        )
        val updateCartItemUseCase = UpdateCartItemUseCase(
            cartRepository,
            productRepository
        )
        val getCartItemsWithPromotionsUseCase = GetCartItemsWithPromotionsUseCase(
            cartRepository,
            productRepository,
            promotionRepository,
            GetPromotionForProduct(),
            clock
        )

        return CartViewModel(
            cartRepository,
            getCarrtSummaryUseCase,
            updateCartItemUseCase,
            getCartItemsWithPromotionsUseCase
        )
    }

    @Test
    fun `given cart data when initialized then emit success state`() = runTest(mainDispatcherRule.scheduler) {
        val productId = "Id1"
        val product = product {
            withId(productId)
            withName("Pan")
            withPrice(2.0)
        }
        val item = cartItem {
            withProductId(productId)
            withQuantity(3)
        }
        val fakeProductRepository = FakeProductRepositoryImpl().apply { setProducts(listOf(product)) }
        val fakeCart = FakeCartRepositoryImpl().apply { setCartItems(listOf(item)) }

        val viewModel = createViewModel(fakeCart,fakeProductRepository)

        viewModel.uiState.test {
            val state = awaitItem() as CartUiState.Success
            assertEquals(1, state.cartItems.size)
            assertEquals(6.0, state.summary?.subTotal)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `given quantity one when decrease quantity then removes item from cart`() = runTest(mainDispatcherRule.scheduler) {
        val productId = "Id1"
        val product = product {
            withId(productId)
            withStock(5)
            withPrice(2.0)
        }
        val item = cartItem {
            withProductId(productId)
            withQuantity(3)
        }
        val fakeProductRepository = FakeProductRepositoryImpl().apply { setProducts(listOf(product)) }
        val fakeCart = FakeCartRepositoryImpl().apply { setCartItems(listOf(item)) }

        val viewModel = createViewModel(fakeCart,fakeProductRepository)

        viewModel.uiState.test {
            awaitItem()

            viewModel.decreaseQuantity(productId, 1)

            val state = awaitItem() as CartUiState.Success

            assertTrue(state.cartItems.isEmpty())
            assertEquals(0.0, state.summary?.finalTotal ?: 0.0, 0.001)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `given insufficient stock when update quantity then emits error event`() = runTest(mainDispatcherRule.scheduler) {
        val productId = "Id1"
        val product = product {
            withId(productId)
            withStock(2)
            withPrice(2.0)
        }
        val item = cartItem {
            withProductId(productId)
            withQuantity(3)
        }
        val fakeProductRepository = FakeProductRepositoryImpl().apply { setProducts(listOf(product)) }
        val fakeCart = FakeCartRepositoryImpl().apply { setCartItems(listOf(item)) }

        val viewModel = createViewModel(fakeCart,fakeProductRepository)

        viewModel.event.test {
            viewModel.increaseQuantity(productId, 5)

            val event = awaitItem()
            assertTrue(event is CartEvent.ShowMessage)

            cancelAndConsumeRemainingEvents()
        }
    }
}