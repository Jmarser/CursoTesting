package com.jmarser.cursotesting.cart.domain.usecase

import com.jmarser.cursotesting.cart.domain.model.CartItem
import com.jmarser.cursotesting.cart.domain.model.CartSummary
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.core.domain.util.Clock
import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.model.ProductPromotion
import com.jmarser.cursotesting.productlist.domain.model.Promotion
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
import com.jmarser.cursotesting.productlist.domain.usecase.GetPromotionForProduct
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
    private val getPromotionForProduct: GetPromotionForProduct,
    private val clock: Clock
) {

    @OptIn(ExperimentalCoroutinesApi::class)
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
                        calculateSummary(cartItems, products, promotions, clock)

                    }
                }
            }
    }

    private fun calculateSummary(
        cartItems: List<CartItem>,
        products: List<Product>,
        promotions: List<Promotion>,
        clock: Clock
    ): CartSummary {
        val now = clock.now()

        val activePromotions = promotions.filter {
            it.startTime <= now && it.endTime >= now
        }

        val productsById = products.associateBy { it.id }
        var subTotal = 0.0
        var discountTotal = 0.0

        for (cartItem: CartItem in cartItems) {
            val product = productsById[cartItem.productId] ?: continue
            val itemTotal = product.price * cartItem.quantity
            subTotal += itemTotal

            discountTotal += calculateDiscountForProduct(
                product = product,
                quantity = cartItem.quantity,
                activePromotions = activePromotions
            )
        }

        val total = (subTotal - discountTotal).coerceAtLeast(0.0)

        return CartSummary(subTotal = subTotal, discountTotal = discountTotal, finalTotal = total)
    }

    private fun calculateDiscountForProduct(
        product: Product,
        quantity: Int,
        activePromotions: List<Promotion>
    ): Double {

        return when (val selectPromotion = getPromotionForProduct(product, activePromotions)) {
            is ProductPromotion.BuyXPayY -> {
                val buy = selectPromotion.buy
                val pay = selectPromotion.pay
                val freePerGroup = (buy - pay).coerceAtLeast(0)
                val groups = quantity / buy
                val freeItems = freePerGroup * groups
                product.price * freeItems
            }

            is ProductPromotion.Percent -> {
                val itemSubtotal = product.price * quantity
                itemSubtotal * (selectPromotion.percent / 100)
            }

            null -> 0.0
        }

    }

}