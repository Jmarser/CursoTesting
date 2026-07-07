package com.jmarser.cursotesting.ProductDetail.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.jmarser.cursotesting.core.mothers.uiState.ProductDetailsUiStateMother
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.ADD_TO_CART_BUTTON_WITHOUT_STOCK
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.ADD_TO_CART_BUTTON_WITH_STOCK
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.MARKET_TOP_APP_BAR
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.MARKET_TOP_APP_BAR_BACK_BUTTON
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_CARD_PRODUCT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_IMAGE_PRODUCT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_CATEGORY
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_DESCRIPTION
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_BUY_PAY
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_PERCENT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_DISPLAY_UNITS_STOCK
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_NAME
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_PRICE
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_PRICE_WITH_DESCOUNT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_STOCK_TITLE
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PROGRESS
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_STATE_LOADING
import org.junit.Assert.assertTrue
import org.junit.Rule
import kotlin.test.Test

class ProductDetailContentScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun createProductDetailsScreen(
        uiState: ProductDetailUiState,
        onBack: () -> Unit = {},
        onAddToCart: () -> Unit = {}
    ){
        composeRule.setContent {
            ProductDetailContentScreen(
                uiState = uiState,
                onBack = onBack,
                onAddToCart = onAddToCart,
            )
        }
    }

    @Test
    fun given_loading_state_when_rendered_then_show_progress(){
        createProductDetailsScreen(uiState = ProductDetailsUiStateMother.isLoading())
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR_BACK_BUTTON).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_STATE_LOADING).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PROGRESS).assertIsDisplayed()
    }

    @Test
    fun given_loading_state_when_onBack_clicked_then_emits_callback(){
        var onBackClicked: Boolean = false
        createProductDetailsScreen(
            uiState = ProductDetailsUiStateMother.isLoading(),
            onBack = { onBackClicked = true}
        )

        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR_BACK_BUTTON).performClick()
        assertTrue(onBackClicked)
    }

    @Test
    fun given_product_details_without_promotion_when_rendered_then_shows_components(){
        createProductDetailsScreen(uiState = ProductDetailsUiStateMother.ProductDetailsWithoutPromotion())
        composeRule.onNodeWithTag(PRODUCT_DETAILS_CARD_PRODUCT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_IMAGE_PRODUCT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_NAME).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_CATEGORY).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DESCRIPTION).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_PRICE).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_UNITS_STOCK).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_STOCK_TITLE).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR_BACK_BUTTON).assertIsDisplayed()
    }

    @Test
    fun given_product_details_without_promotion_without_description_when_rendered_then_shows_components(){
        createProductDetailsScreen(uiState = ProductDetailsUiStateMother.ProductDetailsWithoutPromotionWithoutDescription())
        composeRule.onNodeWithTag(PRODUCT_DETAILS_CARD_PRODUCT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_IMAGE_PRODUCT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_NAME).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_CATEGORY).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DESCRIPTION).assertDoesNotExist()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_PRICE).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_UNITS_STOCK).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_STOCK_TITLE).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR_BACK_BUTTON).assertIsDisplayed()
    }

    @Test
    fun given_product_details_wit_promotion_percent_when_rendered_then_shows_components(){
        createProductDetailsScreen(uiState = ProductDetailsUiStateMother.ProductDetailsWithPromotionPercent())
        composeRule.onNodeWithTag(PRODUCT_DETAILS_CARD_PRODUCT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_IMAGE_PRODUCT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_NAME).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_CATEGORY).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DESCRIPTION).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_PRICE).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_UNITS_STOCK).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_STOCK_TITLE).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR_BACK_BUTTON).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_PRICE_WITH_DESCOUNT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_PERCENT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_BUY_PAY).assertDoesNotExist()
    }

    @Test
    fun given_product_details_wit_promotion_buy_pay_when_rendered_then_shows_components(){
        createProductDetailsScreen(uiState = ProductDetailsUiStateMother.ProductDetailsWithPromotionBuyPay())
        composeRule.onNodeWithTag(PRODUCT_DETAILS_CARD_PRODUCT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_IMAGE_PRODUCT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_NAME).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_CATEGORY).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DESCRIPTION).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_PRICE).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_UNITS_STOCK).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_STOCK_TITLE).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR).assertIsDisplayed()
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR_BACK_BUTTON).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_PRICE_WITH_DESCOUNT).assertIsNotDisplayed()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_PERCENT).assertDoesNotExist()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_BUY_PAY).assertIsDisplayed()
    }

    @Test
    fun given_product_without_stock_when_rendered_then_shows_disabled_no_stock_action(){
        createProductDetailsScreen(uiState = ProductDetailsUiStateMother.ProductDetailsWithoutPromotionWithoutStock())
        composeRule.onNodeWithTag(ADD_TO_CART_BUTTON_WITHOUT_STOCK).isDisplayed()
        composeRule.onNodeWithTag(ADD_TO_CART_BUTTON_WITHOUT_STOCK).assertIsNotEnabled()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_UNITS_STOCK).isDisplayed()
        composeRule.onNodeWithTag(ADD_TO_CART_BUTTON_WITH_STOCK).assertDoesNotExist()

    }

    @Test
    fun given_product_with_stock_when_rendered_then_shows_enabled_stock_action(){
        createProductDetailsScreen(uiState = ProductDetailsUiStateMother.ProductDetailsWithoutPromotion())
        composeRule.onNodeWithTag(ADD_TO_CART_BUTTON_WITH_STOCK).isDisplayed()
        composeRule.onNodeWithTag(ADD_TO_CART_BUTTON_WITH_STOCK).assertIsEnabled()
        composeRule.onNodeWithTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_UNITS_STOCK).isDisplayed()
        composeRule.onNodeWithTag(ADD_TO_CART_BUTTON_WITHOUT_STOCK).assertDoesNotExist()

    }
}