package com.jmarser.cursotesting.cart.data.repository

import com.jmarser.cursotesting.cart.domain.model.CartItem
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.core.domain.model.AppError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeCartRepositoryImpl: CartRepository {

    private val _cartItem = MutableStateFlow<List<CartItem>>(emptyList())

    fun setCartItems(items: List<CartItem>){
        _cartItem.value = items
    }

    override fun getAllCartItems(): Flow<List<CartItem>> = _cartItem.asStateFlow()

    override suspend fun addToCart(productId: String, quantity: Int) {
        val currentItems = _cartItem.value.toMutableList()
        val existingIndex = currentItems.indexOfFirst {
            it.productId == productId
        }

        if (existingIndex >= 0){
            val item = currentItems[existingIndex]

            currentItems[existingIndex] = item.copy(quantity = item.quantity + quantity)
        }else{
            currentItems.add(CartItem(productId, quantity))
        }
        _cartItem.value = currentItems
    }

    override suspend fun deleteCartItem(productId: String) {
        val currentItems = _cartItem.value.toMutableList()
        val existingIndex = currentItems.indexOfFirst {
            it.productId == productId
        }

        if (existingIndex >= 0){
            currentItems.removeAt(existingIndex)
            _cartItem.value = currentItems
        }else{
            throw AppError.NotFoundError
        }
    }

    override suspend fun updateQuantity(productId: String, quantity: Int) {
        val currentItems = _cartItem.value.toMutableList()
        val existingIndex = currentItems.indexOfFirst {
            it.productId == productId
        }

        if (existingIndex >= 0){
            currentItems[existingIndex] = currentItems[existingIndex].copy(quantity = quantity)
            _cartItem.value = currentItems
        }else{
            throw AppError.NotFoundError
        }
    }

    override suspend fun clearCart() {
        _cartItem.value = emptyList()
    }

    override suspend fun getCartItemById(productId: String): CartItem? {
        return _cartItem.value.find { it.productId == productId }
    }

}