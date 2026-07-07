package com.jmarser.cursotesting.checkout.domain.useCase

import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.checkout.domain.model.OrderConfirmation
import com.jmarser.cursotesting.checkout.domain.repository.OrderRepository
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: PlaceOrderUseCase.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 07/07/2026
 */

class PlaceOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(): Result<OrderConfirmation>{
        return try {
            val confirmation = orderRepository.placeOrder()
            cartRepository.clearCart()
            Result.success(confirmation)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}