package com.jmarser.cursotesting.cart.data.mapper

import com.jmarser.cursotesting.cart.data.local.database.entity.CartEntity
import com.jmarser.cursotesting.cart.domain.model.CartItem

/**
 * Project: CursoTesting
 * File: CartItemMapper.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 20/03/2026
 */

fun CartEntity.toDomain(): CartItem{
    return CartItem(
        productId = this.productId,
        quantity = this.quantity
    )
}

fun CartItem.toEntity(): CartEntity{
    return CartEntity(
        productId = this.productId,
        quantity = this.quantity
    )
}