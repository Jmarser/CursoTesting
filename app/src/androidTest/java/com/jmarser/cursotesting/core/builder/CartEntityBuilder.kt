package com.jmarser.cursotesting.core.builder

import com.jmarser.cursotesting.cart.data.local.database.entity.CartEntity
import com.jmarser.cursotesting.cart.domain.model.CartItem

/**
 * Project: CursoTesting
 * File: CartEntityBuilder.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 18/05/2026
 */

class CartEntityBuilder {

    private var productId: String = "id1"
    private var quantity: Int = 2

    fun withProductId(productId: String) = apply { this.productId = productId }
    fun withQuantity(quantity: Int) = apply { this.quantity = quantity }

    fun build() = CartEntity(
        productId = productId,
        quantity = quantity
    )
}

fun cartEntity(block: CartEntityBuilder.() -> Unit = {}) = CartEntityBuilder().apply(block).build()