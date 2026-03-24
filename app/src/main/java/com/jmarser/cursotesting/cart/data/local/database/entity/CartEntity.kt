package com.jmarser.cursotesting.cart.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Project: CursoTesting
 * File: CardItemEntity.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 19/03/2026
 */

@Entity(tableName = "cart_items")
data class CartEntity (

    @PrimaryKey
    val productId: String,
    val quantity: Int
)