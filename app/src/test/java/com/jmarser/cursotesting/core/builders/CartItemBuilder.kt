package com.jmarser.cursotesting.core.builders

import com.jmarser.cursotesting.cart.domain.model.CartItem

/**
 * Project: CursoTesting
 * File: CartItemBuilder.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 27/04/2026
 */

class CartItemBuilder {

    private var productId: String = "id1"
    private var quantity: Int = 2

    fun withProductId(productId: String) = apply { this.productId = productId }
    fun withQuantity(quantity: Int) = apply { this.quantity = quantity }

    fun build() = CartItem(
        productId = productId,
        quantity = quantity
    )
}

fun cartItem(block: CartItemBuilder.() -> Unit = {}) = CartItemBuilder().apply(block).build()