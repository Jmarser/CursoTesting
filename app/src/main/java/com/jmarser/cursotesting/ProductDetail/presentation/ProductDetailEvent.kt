package com.jmarser.cursotesting.ProductDetail.presentation

/**
 * Project: CursoTesting
 * File: ProductDetailEvent.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 19/03/2026
 */

sealed interface ProductDetailEvent {
    data object UNKNOW_ERROR: ProductDetailEvent
    data object NETWORK_ERROR: ProductDetailEvent
    data object INSUFICIENT_STOCK_ERROR: ProductDetailEvent
    data object SUCCESS_ADD_TO_CART: ProductDetailEvent

}