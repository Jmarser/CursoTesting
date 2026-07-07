package com.jmarser.cursotesting.checkout.data.mapper

import com.jmarser.cursotesting.checkout.data.remote.response.OrderConfirmationResponse
import com.jmarser.cursotesting.checkout.domain.model.OrderConfirmation

/**
 * Project: CursoTesting
 * File: OrderConfirmationMapper.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 07/07/2026
 */

fun OrderConfirmationResponse.toDomain(): OrderConfirmation{
    return OrderConfirmation(
        orderId = this.orderId,
        etaMinutes = this.etaMinutes,
        total = this.total
    )
}