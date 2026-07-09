package com.jmarser.cursotesting.checkout.presentation

/**
 * Project: CursoTesting
 * File: CheckoutEvent.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 07/07/2026
 */

sealed interface CheckoutEvent {
    data class ShowMessage(val message: String): CheckoutEvent
}