package com.jmarser.cursotesting.cart.presentation.model

import com.jmarser.cursotesting.cart.domain.model.CartItem
import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion

data class CartItemWithPromotion(
    val cartItem: CartItem,
    val item: ProductWithPromotion
)
