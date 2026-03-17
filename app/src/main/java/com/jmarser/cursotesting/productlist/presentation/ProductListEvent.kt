package com.jmarser.cursotesting.productlist.presentation

/**
 * Project: CursoTesting
 * File: ProductListEvent.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 13/03/2026
 */

sealed interface ProductListEvent {
    data class ShowMessage(val message: String): ProductListEvent
}