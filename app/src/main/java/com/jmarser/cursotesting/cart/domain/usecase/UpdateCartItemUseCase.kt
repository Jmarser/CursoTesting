package com.jmarser.cursotesting.cart.domain.usecase

import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.core.domain.model.AppError
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: UpdateCartItemUseCase.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 20/03/2026
 */

class UpdateCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository
){
    suspend operator fun invoke(productId: String, quantity: Int){

        if (quantity < 0){
            throw AppError.Validation.QuantityMustPositive
        }

        if (quantity == 0){
            cartRepository.deleteCartItem(productId)
            return
        }

        val product = productRepository.getProductById(productId).first() ?: throw AppError.NotFoundError

        if (quantity > product.stock){
            throw AppError.Validation.InsufficientStock(product.stock)
        }

        cartRepository.updateQuantity(productId, quantity)
    }
}