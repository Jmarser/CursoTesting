package com.jmarser.cursotesting.ProductDetail.presentation

/**
 * Project: CursoTesting
 * File: ProductDetailEvent.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 19/03/2026
 */

sealed interface ProductDetailEvent {
    data class ShowMessage(val msg: String): ProductDetailEvent
    data class ShowError(val msg: String): ProductDetailEvent
}