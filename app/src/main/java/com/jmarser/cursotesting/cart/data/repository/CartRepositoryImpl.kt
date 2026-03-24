package com.jmarser.cursotesting.cart.data.repository

import com.jmarser.cursotesting.cart.data.mapper.toDomain
import com.jmarser.cursotesting.cart.data.mapper.toEntity
import com.jmarser.cursotesting.cart.domain.model.CartItem
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.core.domain.model.AppError
import com.jmarser.cursotesting.productlist.data.local.LocalDataSource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Project: CursoTesting
 * File: CartRepositoryImpl.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 19/03/2026
 */

class CartRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
): CartRepository {
    override fun getAllCartItems(): Flow<List<CartItem>> {
        return localDataSource.getAllCartItems()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun addToCart(productId: String, quantity: Int) {
        val existingItem = localDataSource.getCartItemById(productId)
        if ( existingItem != null){
            val newQuantity = existingItem.quantity + quantity
            localDataSource.updateCartItem(existingItem.copy(quantity = newQuantity))
        }else{
            localDataSource.insertcartItem(CartItem(productId, quantity).toEntity())
        }
    }

    override suspend fun deleteCartItem(productId: String) {
        val item = localDataSource.getCartItemById(productId) ?: throw AppError.NotFoundError
        localDataSource.deleteCartItem(item)
    }

    override suspend fun updateQuantity(productId: String, quantity: Int) {
        val item = localDataSource.getCartItemById(productId) ?: throw AppError.NotFoundError
        localDataSource.updateCartItem(item.copy(quantity = quantity))
    }

    override suspend fun clearCart() {
        localDataSource.clearCart()
    }

    override suspend fun getCartItemById(productId: String): CartItem? {
        return localDataSource.getCartItemById(productId)?.toDomain()
    }
}