package com.jmarser.cursotesting.core.mothers.uiState

import com.jmarser.cursotesting.core.mothers.ProductMother
import com.jmarser.cursotesting.core.mothers.PromotionMother
import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion
import com.jmarser.cursotesting.productlist.domain.model.SortOption
import com.jmarser.cursotesting.productlist.presentation.ProductListUiState

object ProductListUiStateMother {

    fun success(
        productsList: List<ProductWithPromotion> = listOf(
            ProductWithPromotion(ProductMother.bread(), PromotionMother.percent()),
            ProductWithPromotion(ProductMother.coffe()),
            ProductWithPromotion(ProductMother.milk()),
        ),
        categories: List<String> = listOf("Bread", "Milk", "Drinks"),
        selectedCategory: String? = null,
        sortOption: SortOption = SortOption.NONE
    ) = ProductListUiState.Success(
        productList = productsList,
        categories = categories,
        selectedCategory = selectedCategory,
        sortOption = sortOption
    )
}