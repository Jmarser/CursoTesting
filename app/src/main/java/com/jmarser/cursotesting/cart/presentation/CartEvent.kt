package com.jmarser.cursotesting.cart.presentation

import android.R

/**
 * Project: CursoTesting
 * File: CartEvent.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 20/03/2026
 */

sealed interface CartEvent {
    data class ShowMessage(val message: String): CartEvent
}