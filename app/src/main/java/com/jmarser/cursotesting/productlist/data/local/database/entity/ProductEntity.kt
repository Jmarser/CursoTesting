package com.jmarser.cursotesting.productlist.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Project: CursoTesting
 * File: ProductEntity.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 16/03/2026
 */

@Entity(tableName = "products")
data class ProductEntity (
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val price: Double,
    val category: String?,
    val stock: Int?,
    val imageUrl: String? = null
)