package com.jmarser.cursotesting.cart.domain.model

data class CartSummary(
    val subTotal: Double,
    val discountTotal: Double,
    val finalTotal: Double
)
