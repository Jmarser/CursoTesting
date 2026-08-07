package com.jmarser.cursotesting.core.mothers.uiState

import com.jmarser.cursotesting.checkout.presentation.CheckoutUiState

object CheckoutUiStateMother {

    fun CheckoutFailed() = CheckoutUiState.Failed(
        message = "Mensaje de error"
    )
}