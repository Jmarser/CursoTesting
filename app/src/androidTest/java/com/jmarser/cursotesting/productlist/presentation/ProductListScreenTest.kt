package com.jmarser.cursotesting.productlist.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jmarser.cursotesting.core.mothers.ProductMother
import com.jmarser.cursotesting.core.mothers.ProductMother.bread
import com.jmarser.cursotesting.core.mothers.ProductMother.coffe
import com.jmarser.cursotesting.core.mothers.ProductMother.milk
import com.jmarser.cursotesting.core.mothers.uiState.ProductListUiStateMother
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.FILTERS_MENU_COMPONENT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.HOME_TOP_APP_BAR_BADGE
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.HOME_TOP_APP_BAR_BUTTON_CART
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.HOME_TOP_APP_BAR_BUTTON_FILTERS
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.HOME_TOP_APP_BAR_BUTTON_SETTINGS
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.HOME_TOP_APP_BAR_COMPONENT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_LIST_STATE_ERROR
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_LIST_STATE_LOADING
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_LIST_STATE_SUCCESS
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_LIST_STATE_SUCCESS_EMPTY
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.productListCategory
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.productListItem
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.productListSortOption
import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion
import com.jmarser.cursotesting.productlist.domain.model.SortOption
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProductListScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun createProductListScreen(
        uiState: ProductListUiState = ProductListUiStateMother.success(),
        cartItemCount: Int = 0,
        filterVisible: Boolean = true,
        onFilterSelected: (Boolean) -> Unit = {},
        onCategorySelected: (String?) -> Unit = {},
        onSortOptionSelected: (SortOption) -> Unit = {},
        navigateToSettings: () -> Unit = {},
        navigateToProductDetail: (ProductWithPromotion) -> Unit = {},
        navigateToCart: () -> Unit = {}
    ) {
        composeRule.setContent {
            ProductListContent(
                uiState = uiState,
                cartItemCount = cartItemCount,
                filterVisible = filterVisible,
                onFilterSelected = onFilterSelected,
                onCategorySelected = onCategorySelected,
                onSortOptionSelected = onSortOptionSelected,
                navigateToSettings = navigateToSettings,
                navigateToProductDetail = navigateToProductDetail,
                navigateToCart = navigateToCart
            )
        }
    }

    @Test
    fun given_loading_state_when_rendered_then_shows_progress_view() {
        createProductListScreen(uiState = ProductListUiState.Loading)
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_COMPONENT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_LIST_STATE_LOADING).assertIsDisplayed()
    }

    @Test
    fun given_error_state_when_rendered_then_shows_error_message() {
        createProductListScreen(uiState = ProductListUiState.Error("Error"))
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_COMPONENT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_LIST_STATE_ERROR).assertIsDisplayed()
    }

    @Test
    fun given_success_state_without_products_when_rendered_then_shows_products_not_found_message() {
        createProductListScreen(
            uiState = ProductListUiState.Success(
                productList = emptyList(),
                categories = emptyList(),
                selectedCategory = null,
                sortOption = SortOption.NONE
            )
        )
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_COMPONENT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_LIST_STATE_SUCCESS_EMPTY).assertIsDisplayed()
    }

    @Test
    fun given_success_state_with_products_when_rendered_then_shows_products_list() {
        createProductListScreen(uiState = ProductListUiStateMother.success())
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_COMPONENT).assertIsDisplayed()
        composeRule.onNodeWithTag(PRODUCT_LIST_STATE_SUCCESS).assertIsDisplayed()
        composeRule.onNodeWithTag(productListItem(bread().id)).assertIsDisplayed()
        composeRule.onNodeWithTag(productListItem(milk().id)).assertIsDisplayed()
        composeRule.onNodeWithTag(productListItem(coffe().id)).assertIsDisplayed()

    }

    @Test
    fun given_filters_menu_hiden_when_toggleClicked_then_emitTrue() {
        var emitted: Boolean? = null
        createProductListScreen(
            uiState = ProductListUiStateMother.success(),
            filterVisible = false,
            onFilterSelected = { newState -> emitted = newState }
        )
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_BUTTON_FILTERS).performClick()
        assertEquals(true,emitted)
    }

    @Test
    fun given_filters_menu_visible_when_toggleClicked_then_emitFalse() {
        var emitted: Boolean? = null
        createProductListScreen(
            uiState = ProductListUiStateMother.success(),
            filterVisible = true,
            onFilterSelected = { newState -> emitted = newState }
        )
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_BUTTON_FILTERS).performClick()
        assertEquals(false,emitted)
    }

    @Test
    fun given_product_list_rendered_when_settings_icon_clicked_then_emit_callback() {
        var settingsCalled = false
        createProductListScreen(
            uiState = ProductListUiStateMother.success(),
            navigateToSettings = { settingsCalled = true }
        )
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_BUTTON_SETTINGS).performClick()
        assertTrue(settingsCalled)
    }

    @Test
    fun given_cart_button_when_clicked_then_invoke_navigate_to_cart_callback() {
        var cartCalled = false
        createProductListScreen(
            uiState = ProductListUiStateMother.success(),
            navigateToCart = {
                cartCalled = true
            }
        )
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_BUTTON_CART).performClick()
        assertTrue(cartCalled)
    }

    @Test
    fun given_productItem_when_clicked_then_invoke_navigateToProductDetail_callback_with_correct_product() {
        var clickedProduct: ProductWithPromotion? = null
        createProductListScreen(
            uiState = ProductListUiStateMother.success(),
            navigateToProductDetail = { product ->
                clickedProduct = product
            }
        )

        val expectedId = ProductMother.coffe().id
        composeRule.onNodeWithTag(productListItem(ProductMother.coffe().id)).performClick()
        assertNotNull(clickedProduct)
        assertEquals(expectedId, clickedProduct.product.id)
    }

    // FILTERS MENU

    @Test
    fun given_filter_visible_false_when_rendered_then_filter_menu_not_dislayed(){
        createProductListScreen(
            uiState = ProductListUiStateMother.success(),
            filterVisible = false
        )
        composeRule.onNodeWithTag(FILTERS_MENU_COMPONENT).assertDoesNotExist()
    }

    // CATEGORIES
    @Test
    fun given_no_category_selected_when_rendered_then_mark_all_chip() {
        createProductListScreen(uiState = ProductListUiStateMother.success(selectedCategory = null))
        composeRule.onNodeWithTag(productListCategory(null)).assertIsSelected()
    }

    @Test
    fun given_category_selected_when_rendered_then_mark_that_chip() {
        createProductListScreen(uiState = ProductListUiStateMother.success(selectedCategory = "Drinks"))
        composeRule.onNodeWithTag(productListCategory("Drinks")).assertIsSelected()
    }

    // SORT OPTIONS
    @Test
    fun given_not_sort_option_selected_when_rendered_then_not_mark_sorter_chip() {
        createProductListScreen(uiState = ProductListUiStateMother.success(sortOption = SortOption.NONE))
        composeRule.onNodeWithTag(productListSortOption(SortOption.PRICE_ASC)).assertIsNotSelected()
        composeRule.onNodeWithTag(productListSortOption(SortOption.PRICE_DESC))
            .assertIsNotSelected()
        composeRule.onNodeWithTag(productListSortOption(SortOption.DISCOUNT)).assertIsNotSelected()
    }

    @Test
    fun given_sort_option_selected_when_rendered_then_mark_sort_option_selected_chip() {
        createProductListScreen(uiState = ProductListUiStateMother.success(sortOption = SortOption.PRICE_DESC))
        composeRule.onNodeWithTag(productListSortOption(SortOption.PRICE_ASC)).assertIsNotSelected()
        composeRule.onNodeWithTag(productListSortOption(SortOption.PRICE_DESC)).assertIsSelected()
        composeRule.onNodeWithTag(productListSortOption(SortOption.DISCOUNT)).assertIsNotSelected()
    }

    @Test
    fun given_product_list_rendered_when_selected_sort_discount_option_then_mark_chip_sort_discount_option() {
        val sortOptionToSelect = SortOption.DISCOUNT
        var sortOptionSelected: SortOption = SortOption.NONE

        createProductListScreen(
            uiState = ProductListUiStateMother.success(
                sortOption = SortOption.NONE,
            ),
            onSortOptionSelected = { sortOption -> sortOptionSelected = sortOption }
        )

        composeRule.onNodeWithTag(productListSortOption(sortOptionToSelect)).performClick()

        assertEquals(sortOptionToSelect, sortOptionSelected)
    }

    @Test
    fun given_product_list_rendered_when_selected_sort_price_asc_option_then_mark_chip_sort_price_asc_option() {
        val sortOptionToSelect = SortOption.PRICE_ASC
        var sortOptionSelected: SortOption = SortOption.NONE

        createProductListScreen(
            uiState = ProductListUiStateMother.success(
                sortOption = SortOption.NONE,
            ),
            onSortOptionSelected = { sortOption -> sortOptionSelected = sortOption }
        )

        composeRule.onNodeWithTag(productListSortOption(sortOptionToSelect)).performClick()

        assertEquals(sortOptionToSelect, sortOptionSelected)
    }

    @Test
    fun given_product_list_rendered_when_selected_sort_price_desc_option_then_mark_chip_sort_price_desc_option() {
        val sortOptionToSelect = SortOption.PRICE_DESC
        var sortOptionSelected: SortOption = SortOption.NONE

        createProductListScreen(
            uiState = ProductListUiStateMother.success(
                sortOption = SortOption.NONE,
            ),
            onSortOptionSelected = { sortOption -> sortOptionSelected = sortOption }
        )

        composeRule.onNodeWithTag(productListSortOption(sortOptionToSelect)).performClick()

        assertEquals(sortOptionToSelect, sortOptionSelected)
    }

    @Test
    fun given_cart_item_count_zero_when_rendered_then_hides_badge(){
        createProductListScreen(cartItemCount = 0)
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_BADGE).assertDoesNotExist()
    }

    @Test
    fun given_cart_item_count_positive_when_rendered_then_shows_badge_with_count(){
        createProductListScreen(cartItemCount = 10)
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_BADGE).assertIsDisplayed()
        composeRule.onNodeWithText("10").assertIsDisplayed()
    }

    @Test
    fun given_cart_item_count_over_99_when_rendered_then_shows_99_plus(){
        createProductListScreen(cartItemCount = 150)
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_BADGE).assertIsDisplayed()
        composeRule.onNodeWithText("99+").assertIsDisplayed()
    }

    @Test
    fun given_error_state_when_rendered_then_filters_menu_hide(){
        createProductListScreen(
            uiState = ProductListUiState.Error("Error"),
            filterVisible = true
        )
        composeRule.onNodeWithTag(FILTERS_MENU_COMPONENT).assertDoesNotExist()
    }

    @Test
    fun given_error_state_rendered_when_filters_menu_clicked_then_filter_menu_not_dislayed(){
        var emitted: Boolean? = null
        createProductListScreen(
            uiState = ProductListUiState.Error("Error"),
            filterVisible = false,
            onFilterSelected = { newState -> emitted = newState }
        )
        composeRule.onNodeWithTag(HOME_TOP_APP_BAR_BUTTON_FILTERS).performClick()

        composeRule.onNodeWithTag(FILTERS_MENU_COMPONENT).assertDoesNotExist()
        assertEquals(true,emitted)
    }

}