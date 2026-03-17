package com.jmarser.cursotesting.productlist.presentation

import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.model.SortOption

/**
 * Project: CursoTesting
 * File: ProductListUiState.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 13/03/2026
 */

sealed class ProductListUiState {
    data object Loading : ProductListUiState()
    data class Error(val message: String) : ProductListUiState()
    data class Success(
        val productList: List<Product>,
        val categories: List<String>,
        val selectedCategory: String?,
        val sortOption: SortOption
    ) : ProductListUiState()
}