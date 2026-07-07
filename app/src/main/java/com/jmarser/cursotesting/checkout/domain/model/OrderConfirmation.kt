package com.jmarser.cursotesting.checkout.domain.model

data class OrderConfirmation(
    val orderId: String,
    val etaMinutes: Int,
    val total: Double
)
