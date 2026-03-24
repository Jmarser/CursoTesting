package com.jmarser.cursotesting.cart.domain.repository

import com.jmarser.cursotesting.cart.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

/**
 * Project: CursoTesting
 * File: CartRepository.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 19/03/2026
 */

interface CartRepository {

    fun getAllCartItems(): Flow<List<CartItem>>

    suspend fun addToCart(productId: String, quantity: Int)

    suspend fun deleteCartItem(productId: String)

    suspend fun updateQuantity(productId: String, quantity: Int)

    suspend fun clearCart()

    suspend fun getCartItemById(productId: String): CartItem?
}