package com.jmarser.cursotesting.cart.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import com.jmarser.cursotesting.core.mothers.ProductMother.bread
import com.jmarser.cursotesting.core.mothers.ProductMother.coffe
import com.jmarser.cursotesting.core.mothers.ProductMother.milk
import com.jmarser.cursotesting.core.mothers.uiState.CartUiStateMother
import com.jmarser.cursotesting.core.mothers.uiState.CartUiStateMother.cartItemWithPromotion
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CART_STATE_ERROR_RETRY_BUTTON
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CART_STATE_LOADING
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CART_STATE_SUCESS_EMPTY
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.MARKET_TOP_APP_BAR
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.MARKET_TOP_APP_BAR_BACK_BUTTON
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.cartItem
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.cartQuantityDecrease
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.cartQuantityIncrease
import org.junit.Assert.assertTrue
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class CartScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun createCartScreen(
        uiState: CartUiState,
        onBack: () -> Unit = {},
        onRetry: () -> Unit = {},
        onIncreaseQuantity: (String, Int) -> Unit = { _, _ -> },
        onDecreaseQuantity: (String, Int) -> Unit = { _, _ -> },
        onRemoveItem: (String) -> Unit = {}
    ) {
        composeRule.setContent {
            CartContentScreen(
                uiState = uiState,
                onBack = onBack,
                onRetry = onRetry,
                onIncreaseQuantity = onIncreaseQuantity,
                onDecreaseQuantity = onDecreaseQuantity,
                onRemoveItem = onRemoveItem
            )
        }
    }

    @Test
    fun given_loading_state_when_rendered_then_show_progress() {
        createCartScreen(uiState = CartUiState.Loading)
        composeRule.onNodeWithTag(CART_STATE_LOADING).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR).assertIsDisplayed()
    }

    @Test
    fun given_loading_state_when_onBack_clicked_then_emits_callBack() {
        var onBackClicked: Boolean = false
        createCartScreen(
            uiState = CartUiState.Loading,
            onBack = { onBackClicked = true }
        )

        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR_BACK_BUTTON).performClick()
        assertTrue(onBackClicked)
    }

    @Test
    fun given_error_state_when_rendered_then_show_text_and_retry_button() {
        val errorText = "Prueba error"
        createCartScreen(uiState = CartUiState.Error(errorText))

        composeRule.onNodeWithText(errorText, substring = true).assertIsDisplayed()
        composeRule.onNodeWithTag(CART_STATE_ERROR_RETRY_BUTTON).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR).assertIsDisplayed()
    }

    @Test
    fun given_error_state_when_retry_clicked_then_emits_retry_callBack() {
        val errorText = "Prueba error"
        var retryClicked: Boolean = false
        createCartScreen(
            uiState = CartUiState.Error(errorText),
            onRetry = { retryClicked = true }
        )

        composeRule.onNodeWithTag(CART_STATE_ERROR_RETRY_BUTTON).performClick()

        assertTrue(retryClicked)
    }

    @Test
    fun given_error_state_when_onBack_clicked_then_emits_callBack() {
        var onBackClicked: Boolean = false
        createCartScreen(
            uiState = CartUiState.Error("Prueba error"),
            onBack = { onBackClicked = true }
        )

        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR_BACK_BUTTON).performClick()
        assertTrue(onBackClicked)
    }

    @Test
    fun given_empty_success_state_when_rendered_then_shows_empty_cart_message() {
        createCartScreen(
            uiState = CartUiState.Success(
                summary = null,
                cartItems = emptyList(),
                isLoading = false
            )
        )

        composeRule.onNodeWithTag(CART_STATE_SUCESS_EMPTY).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR).assertIsDisplayed()
    }

    @Test
    fun given_empty_success_state_when_onBack_clicked_then_emits_callBack() {
        var onBackClicked: Boolean = false
        createCartScreen(
            uiState = CartUiState.Success(
                summary = null,
                cartItems = emptyList(),
                isLoading = false
            ),
            onBack = { onBackClicked = true }
        )

        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR_BACK_BUTTON).performClick()
        assertTrue(onBackClicked)
    }

    @Test
    fun given_success_state_when_rendered_then_shows_items_quantities_and_summary(){
        createCartScreen(uiState = CartUiStateMother.cartSuccess())

        composeRule.onNodeWithTag(cartItem(coffe().id)).assertIsDisplayed()
        composeRule.onNodeWithTag(cartItem(bread().id)).assertIsDisplayed()
        composeRule.onNodeWithTag(cartItem(milk().id)).assertIsDisplayed()
        composeRule.onNodeWithText(coffe().name).assertIsDisplayed()
        composeRule.onNodeWithText(bread().name).assertIsDisplayed()
        composeRule.onNodeWithText(milk().name).assertIsDisplayed()
        composeRule.onNodeWithText("Subtotal").assertIsDisplayed()
        composeRule.onNodeWithText("Descuento:").assertIsDisplayed()
        composeRule.onNodeWithText("Total:").assertIsDisplayed()
    }

    @Test
    fun given_success_state_when_onBack_clicked_then_emits_callBack() {
        var onBackClicked: Boolean = false
        createCartScreen(
            uiState = CartUiStateMother.cartSuccess(),
            onBack = { onBackClicked = true }
        )

        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR_BACK_BUTTON).performClick()
        assertTrue(onBackClicked)
    }

    @Test
    fun given_initial_quantity_when_increase_clicked_then_emits_increase_quantity(){
        var emitted: Pair<String, Int>? = null
        val initailQuantity = 2

        createCartScreen(
            uiState = CartUiStateMother.cartSuccess(
                cartItems = listOf(
                    cartItemWithPromotion(
                        product = bread(),
                        quantity = initailQuantity
                    )
                )
            ),
            onIncreaseQuantity = {productId, quantity -> emitted = productId to quantity}
        )

        composeRule.onNodeWithTag(cartQuantityIncrease(bread().id))
            .assertIsEnabled()
            .performClick()

        assertEquals(bread().id to initailQuantity, emitted)
    }

    @Test
    fun given_initial_quantity_when_decrease_clicked_then_emits_decrease_quantity(){
        var emitted: Pair<String, Int>? = null
        val initailQuantity = 3

        createCartScreen(
            uiState = CartUiStateMother.cartSuccess(
                cartItems = listOf(
                    cartItemWithPromotion(
                        product = bread(),
                        quantity = initailQuantity
                    )
                )
            ),
            onDecreaseQuantity = {productId, quantity -> emitted = productId to quantity}
        )

        composeRule.onNodeWithTag(cartQuantityDecrease(bread().id))
            .assertIsEnabled()
            .performClick()

        assertEquals(bread().id to initailQuantity, emitted)
    }

    @Test
    fun give_cartItem_when_swipedRight_then_emits_remove_callBack(){
        var removeProductId: String? = null

        createCartScreen(
            uiState = CartUiStateMother.cartSuccess(
                cartItems = listOf(
                    cartItemWithPromotion(
                        product = bread(),
                        quantity = 2
                    ),
                    cartItemWithPromotion(
                        product = milk(),
                        quantity = 3
                    ),
                )
            ),
            onRemoveItem = {removeProductId = it}
        )

        composeRule.onNodeWithTag(cartItem(milk().id)).performTouchInput {
            swipeRight()
        }

        composeRule.waitUntil(timeoutMillis = 3000){
            removeProductId != null
        }

        assertEquals(milk().id, removeProductId)
    }

    @Test
    fun given_items_at_stock_edges_when_rendered_then_invalid_controlls_are_Disable(){
        val fullStockItem = cartItemWithPromotion(
            product = bread(stock = 7),
            quantity = 7
        )

        createCartScreen(uiState = CartUiStateMother.cartSuccess(cartItems = listOf(fullStockItem)))

        composeRule.onNodeWithTag(cartQuantityIncrease(bread().id)).assertIsNotEnabled()
    }
}