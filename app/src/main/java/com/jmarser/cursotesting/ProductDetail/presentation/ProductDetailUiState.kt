package com.jmarser.cursotesting.ProductDetail.presentation

import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion

data class ProductDetailUiState(
    val item: ProductWithPromotion? = null,
    val isLoading: Boolean = true
)
