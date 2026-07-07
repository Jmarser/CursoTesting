package com.jmarser.cursotesting.checkout.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class OrderConfirmationResponse(

    @SerialName(value = "orderId")
    val orderId: String,
    @SerialName(value = "orderId")
    val etaMinutes: Int,
    @SerialName(value = "orderId")
    val total: Double,
)