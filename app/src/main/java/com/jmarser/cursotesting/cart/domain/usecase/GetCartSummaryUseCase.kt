package com.jmarser.cursotesting.cart.domain.usecase

import com.jmarser.cursotesting.cart.domain.model.CartItem
import com.jmarser.cursotesting.cart.domain.model.CartSummary
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.model.ProductPromotion
import com.jmarser.cursotesting.productlist.domain.model.Promotion
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
import com.jmarser.cursotesting.productlist.domain.usecase.GetPromotionForProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: getCartSummaryUseCase.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 20/03/2026
 */

class GetCartSummaryUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    private val promotionRepository: PromotionRepository,
    private val getPromotionForProduct: GetPromotionForProduct
) {

    operator fun invoke(): Flow<CartSummary> {
        return cartRepository.getAllCartItems()
            .flatMapLatest { cartItems ->
                val ids = cartItems.mapTo(mutableSetOf()) { it.productId }
                if (ids.isEmpty()) {
                    flowOf(CartSummary(subTotal = 0.0, discountTotal = 0.0, finalTotal = 0.0))
                } else {
                    combine(
                        productRepository.getProductsByIds(ids),
                        promotionRepository.getActivePromotions()
                    ) { products, promotions ->
                        calculateSummary(cartItems, products, promotions)

                    }
                }

            }
    }

    private fun calculateSummary(
        cartItems: List<CartItem>,
        products: List<Product>,
        promotions: List<Promotion>
    ): CartSummary{
        val now = Instant.now()

        val activePromotions = promotions.filter {
            it.startTime <= now && it.endTime >= now
        }

        val productsById = products.associateBy { it.id }
        var subTotal = 0.0
        var discountTotal = 0.0

        for(cartItem: CartItem in cartItems){
            val product = productsById[cartItem.productId] ?: continue
            val itemTotal = product.price * cartItem.quantity
            subTotal += itemTotal

            discountTotal += calculateDiscountForProduct(product = product, quantity = cartItem.quantity, activePromotions = activePromotions)
        }

        val total = (subTotal - discountTotal).coerceAtLeast(0.0)

        return CartSummary(subTotal = subTotal, discountTotal = discountTotal, finalTotal = total)
    }

    private fun calculateDiscountForProduct(
        product: Product,
        quantity: Int,
        activePromotions: List<Promotion>
    ): Double {

        return when(val selecctPromotion = getPromotionForProduct(product, activePromotions)){
            is ProductPromotion.BuyXPayY -> {
                val buy = selecctPromotion.buy
                val pay = selecctPromotion.pay
                val freePerGroup = (buy-pay).coerceAtLeast(0)
                val groups = quantity/buy
                val freeItems = freePerGroup * groups
                product.price * freeItems
            }
            is ProductPromotion.Percent -> {
                val itemSubtotal = product.price * quantity
                itemSubtotal * (selecctPromotion.percent/100)
            }
            null -> 0.0
        }

    }

}