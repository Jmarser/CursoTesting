package com.jmarser.cursotesting.checkout.presentation

import com.jmarser.cursotesting.cart.domain.model.CartSummary
import com.jmarser.cursotesting.checkout.domain.model.OrderConfirmation

/**
 * Project: CursoTesting
 * File: CheckoutUiState.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 07/07/2026
 */

sealed class CheckoutUiState {

    data object Loading : CheckoutUiState()
    data class Success(val confirmation: OrderConfirmation) : CheckoutUiState()
    data class Failed(val message: String): CheckoutUiState()
    data class Idle(
        val summary: CartSummary,
        val form: CheckoutForm,
        val errors: CheckoutFormErrors,
        val isCartEmpty: Boolean,
        val isSubmitting: Boolean,
        val canSubmit: Boolean
    ): CheckoutUiState()
}