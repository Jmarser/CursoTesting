package com.jmarser.cursotesting.core.presentation.testing

import com.jmarser.cursotesting.productlist.domain.model.SortOption

object UiTestTag {

    const val TOP_APP_BAR = "top_app_bar"

    /** SETTINGS SCREEN */
    const val SETTINGS_CONTENT = "settings_content"
    const val SETTINGS_IN_STOCK_SWITCH = "settings_in_stock_switch"
    const val SETTINGS_TAX_SWITCH = "settings_tax_switch"
    const val SETTINGS_SEGMENT_BUTTON = "settings_segment_button"
    const val SETTINGS_SEGMENT_BUTTON_OPTION_SYSTEM = "settings_segment_button_option_system"
    const val SETTINGS_SEGMENT_BUTTON_OPTION_CLEAR = "settings_segment_button_option_clear"
    const val SETTINGS_SEGMENT_BUTTON_OPTION_DARK = "settings_segment_button_option_dark"

    /** PRODUCT LIST SCREEN*/
    const val PRODUCT_LIST_STATE_LOADING = "product_list_state_loading"
    const val PRODUCT_LIST_STATE_ERROR = "product_list_state_error"
    const val PRODUCT_LIST_STATE_SUCCESS_EMPTY = "product_list_state_success_empty"
    const val PRODUCT_LIST_STATE_SUCCESS = "product_list_state_success"

    /** HOME TOP APP BAR COMPONENT */
    const val HOME_TOP_APP_BAR_COMPONENT = "home_top_app_bar_component"
    const val HOME_TOP_APP_BAR_BUTTON_FILTERS = "home_top_app_bar_button_filters"
    const val HOME_TOP_APP_BAR_BUTTON_SETTINGS = "home_top_app_bar_button_settings"
    const val HOME_TOP_APP_BAR_BUTTON_CART = "home_top_app_bar_button_cart"
    const val HOME_TOP_APP_BAR_BADGE = "home_top_app_bar_badge"

    fun productListItem(productId: String) = "product_list_item_$productId"

    const val FILTERS_MENU_COMPONENT = "filter_menu_component"
    fun productListCategory(category: String?) = "product_list_category_${category ?: "Todas"}"
    fun productListSortOption(sortOption: SortOption) = "product_list_sort_option_${sortOption.name}"

    /** CART SETTINGS */
    const val CART_STATE_LOADING = "cart_state_loading"
    const val CART_STATE_ERROR = "cart_state_error"
    const val CART_STATE_ERROR_RETRY_BUTTON = "cart_state_error_retry_button"
    const val CART_STATE_SUCESS = "cart_state_success"
    const val CART_STATE_SUCESS_EMPTY = "cart_state_success_empty"

    /** MARKET TOP APP BAR */
    const val MARKET_TOP_APP_BAR = "market_top_app_bar"
    const val MARKET_TOP_APP_BAR_BACK_BUTTON = "market_top_app_bar_back_button"

    fun cartItem(productId: String) = "cart_item_$productId"
    fun cartQuantityIncrease(productId: String) = "cart_quantity_increase_$productId"
    fun cartQuantityDecrease(productId: String) = "cart_quantity_decrease_$productId"

    /** PRODUCT DETAILS */
    const val PRODUCT_DETAILS_STATE_LOADING = "product_details_state_loading"
    const val PRODUCT_DETAILS_PROGRESS = "product_details_progress"
    const val PRODUCT_DETAILS_CARD_PRODUCT = "product_details_card_product"
    const val PRODUCT_DETAILS_IMAGE_PRODUCT = "product_details_image_product"
    const val PRODUCT_DETAILS_PRODUCT_NAME = "product_details_product_name"
    const val PRODUCT_DETAILS_PRODUCT_CATEGORY = "product_details_product_category"
    const val PRODUCT_DETAILS_PRODUCT_DESCRIPTION = "product_details_product_description"
    const val PRODUCT_DETAILS_PRODUCT_PRICE = "product_details_product_price"
    const val PRODUCT_DETAILS_PRODUCT_PRICE_WITH_DESCOUNT = "product_details_product_price_with_descount"
    const val PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_PERCENT = "producct_details_product_display_promotion_percent"
    const val PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_BUY_PAY = "producct_details_product_display_promotion_buy_pay"
    const val PRODUCT_DETAILS_PRODUCT_DISPLAY_UNITS_STOCK = "product_details_product_display_units_stock"
    const val PRODUCT_DETAILS_PRODUCT_STOCK_TITLE = "product_details_stock_title"
    const val ADD_TO_CART_BUTTON_WITH_STOCK = "add_to_cart_button_with_stock"
    const val ADD_TO_CART_BUTTON_WITHOUT_STOCK = "add_to_cart_button_without_stock"
}