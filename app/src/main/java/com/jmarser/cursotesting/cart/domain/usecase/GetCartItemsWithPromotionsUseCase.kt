package com.jmarser.cursotesting.cart.domain.usecase

import com.jmarser.cursotesting.cart.domain.ex.activeAt
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.cart.presentation.model.CartItemWithPromotion
import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
import com.jmarser.cursotesting.productlist.domain.usecase.GetPromotionForProduct
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.time.Instant

/**
 * Project: CursoTesting
 * File: GetCartItemsWithPromotionsUseCase.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 23/03/2026
 */

class GetCartItemsWithPromotionsUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    private val promotionRepository: PromotionRepository,
    private val getPromotionForProduct: GetPromotionForProduct
) {

    operator fun invoke(): Flow<List<CartItemWithPromotion>> {
        return cartRepository.getAllCartItems().flatMapLatest { cartItems ->
            val ids = cartItems.mapTo(mutableSetOf()) {
                it.productId
            }
            if (ids.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    productRepository.getProductsByIds(ids),
                    promotionRepository.getActivePromotions()
                ) { products, promotions ->
                    val activePromotions = promotions.activeAt(Instant.now())
                    val productsById = products.associateBy { it.id }
                    cartItems.mapNotNull { cartItem ->
                        val product = productsById[cartItem.productId] ?: return@mapNotNull null
                        val promotion = getPromotionForProduct(product, activePromotions)
                        val productWithPromotion = ProductWithPromotion(product, promotion)
                        CartItemWithPromotion(
                            cartItem = cartItem,
                            item = productWithPromotion
                        )
                    }
                }
            }

        }
    }

}