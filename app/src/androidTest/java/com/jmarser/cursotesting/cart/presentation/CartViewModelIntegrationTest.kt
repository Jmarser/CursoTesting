package com.jmarser.cursotesting.cart.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.cart.domain.usecase.GetCartItemsWithPromotionsUseCase
import com.jmarser.cursotesting.cart.domain.usecase.GetCartSummaryUseCase
import com.jmarser.cursotesting.cart.domain.usecase.UpdateCartItemUseCase
import com.jmarser.cursotesting.core.mockwebserver.MarketApiDispatcher
import com.jmarser.cursotesting.core.mockwebserver.MockWebServerUrlHolder
import com.jmarser.cursotesting.core.mockwebserver.rules.MockWebServerRule
import com.jmarser.cursotesting.core.utils.MainDispatcherRule
import com.jmarser.cursotesting.core.utils.asAsset
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CartViewModelIntegrationTest {

    private companion object{
        const val PRODUCT_ID = "p1"
        const val UPDATE_QUANTITY = 2
    }

    @get:Rule(order = 0)
    val mockWebServer = MockWebServerRule()

    @get:Rule(order = 1)
    val hilt = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val mainDispacherRule = MainDispatcherRule()

    @Inject
    lateinit var cartRepository: CartRepository

    @Inject
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var promotionRepository: PromotionRepository

    @Inject
    lateinit var getCartSummaryUseCase: GetCartSummaryUseCase

    @Inject
    lateinit var updateCartItemUseCase: UpdateCartItemUseCase

    @Inject
    lateinit var getCartItemsWithPromotionsUseCase: GetCartItemsWithPromotionsUseCase


    @Before
    fun setUp() = runTest{
        mockWebServer.server.dispatcher = MarketApiDispatcher(productJson = "product_list_default.json".asAsset())
        hilt.inject()
        cartRepository.clearCart()
        productRepository.refreshProduct()
        promotionRepository.refreshPromotions()
    }

    @After
    fun tearDown() {
        MockWebServerUrlHolder.baseUrl = "http://localhost:8080/"
    }

    @Test
    fun given_cartWithItems_when_viewModel_collectsUiState_then_success_with_summary() = runTest {
        cartRepository.addToCart(PRODUCT_ID, UPDATE_QUANTITY)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val result = awaitSuccessMatching { state ->
                state.summary != null && state.cartItems.isNotEmpty()
            }

            assertTrue(result.cartItems.isNotEmpty())
            assertTrue(result.summary != null)
            assertEquals(20.0, result.summary?.subTotal ?: 0.0, 0.01)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun given_singleProduct_when_increaseQuantity_then_quantity_updates() = runTest {
        cartRepository.addToCart(PRODUCT_ID, 1)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val result = awaitSuccessMatching { state ->
                state.cartItems.any { it.cartItem.productId == PRODUCT_ID && it.cartItem.quantity == 1 }
            }

            assertEquals(1, result.cartItems.first().cartItem.quantity)

            viewModel.increaseQuantity(PRODUCT_ID, 1)

            val updatedState = awaitSuccessMatching { state ->
                state.cartItems.any { it.cartItem.productId == PRODUCT_ID && it.cartItem.quantity == 2 }
            }

            assertEquals(2, updatedState.cartItems.first().cartItem.quantity)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun given_singleProduct_when_decrease_to_zero_then_cart_becomes_empty() = runTest {
        cartRepository.addToCart(PRODUCT_ID, 1)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val result = awaitSuccessMatching { state ->
                state.cartItems.any {
                    it.cartItem.productId == PRODUCT_ID && it.cartItem.quantity == 1
                }
            }
            assertEquals(1, result.cartItems.first().cartItem.quantity)

            viewModel.decreaseQuantity(PRODUCT_ID, 1)

            val updateState = awaitSuccessMatching { state ->
                state.cartItems.isEmpty()
            }

            assertTrue(updateState.cartItems.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel(): CartViewModel{
        return CartViewModel(
            cartRepository = cartRepository,
            getCartSummaryUseCase = getCartSummaryUseCase,
            updateCartItemUseCase = updateCartItemUseCase,
            getCartItemsWithPromotionsUseCase = getCartItemsWithPromotionsUseCase
        )
    }

    private suspend fun ReceiveTurbine<CartUiState>.awaitSuccessMatching(
        predicate: (CartUiState.Success) -> Boolean
    ): CartUiState.Success{
        while(true){
            when(val item = awaitItem()){
                is CartUiState.Success -> if(predicate(item)) return item
                is CartUiState.Error -> error("Unexpected error: ${item.message}")
                is CartUiState.Loading -> Unit
            }
        }
    }
}