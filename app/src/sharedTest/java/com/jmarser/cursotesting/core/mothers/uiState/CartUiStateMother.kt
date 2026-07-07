package com.jmarser.cursotesting.core.mothers.uiState

import com.jmarser.cursotesting.cart.domain.model.CartItem
import com.jmarser.cursotesting.cart.domain.model.CartSummary
import com.jmarser.cursotesting.cart.presentation.CartUiState
import com.jmarser.cursotesting.cart.presentation.model.CartItemWithPromotion
import com.jmarser.cursotesting.core.mothers.ProductMother.bread
import com.jmarser.cursotesting.core.mothers.ProductMother.coffe
import com.jmarser.cursotesting.core.mothers.ProductMother.milk
import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.model.ProductPromotion
import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion

object CartUiStateMother {
    fun cartSuccess(
        cartItems: List<CartItemWithPromotion> = listOf(
            cartItemWithPromotion(product = bread(), quantity = 2),
            cartItemWithPromotion(product = coffe(), quantity = 5),
            cartItemWithPromotion(product = milk(), quantity = 1),
        ),
        summary: CartSummary = CartSummary(
            subTotal = 10.3,
            discountTotal = 1.7,
            finalTotal = 11.0
        ),
        isLoading: Boolean = false
    ) = CartUiState.Success(
        summary = summary,
        cartItems = cartItems,
        isLoading = isLoading
    )

    fun cartItemWithPromotion(
        product: Product,
        quantity: Int,
        promotion: ProductPromotion? = null
    ) = CartItemWithPromotion(
        cartItem = CartItem(
            productId = product.id, quantity = quantity
        ),
        item = ProductWithPromotion(product, promotion)
    )
}