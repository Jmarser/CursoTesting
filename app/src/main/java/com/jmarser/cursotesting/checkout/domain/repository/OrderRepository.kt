package com.jmarser.cursotesting.checkout.domain.repository

import com.jmarser.cursotesting.checkout.domain.model.OrderConfirmation

/**
 * Project: CursoTesting
 * File: OrderRepository.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 07/07/2026
 */

interface OrderRepository {

    suspend fun placeOrder(): OrderConfirmation

}