package com.jmarser.cursotesting.cart.presentation

import com.jmarser.cursotesting.cart.domain.model.CartSummary
import com.jmarser.cursotesting.cart.presentation.model.CartItemWithPromotion

sealed class CartUiState {
    data class Success(
        val summary: CartSummary,
        val cartItems: List<CartItemWithPromotion>,
        val isLoading: Boolean
    ) : CartUiState()

    data object Loading: CartUiState()

    data class Error(val message: String): CartUiState()
}
