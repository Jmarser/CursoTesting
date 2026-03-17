package com.jmarser.cursotesting.productlist.data.mappers

import com.jmarser.cursotesting.productlist.data.local.database.entity.ProductEntity
import com.jmarser.cursotesting.productlist.data.remote.model.ProductResponse
import com.jmarser.cursotesting.productlist.domain.model.Product

/**
 * Project: CursoTesting
 * File: ProductMapper.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 16/03/2026
 */

fun ProductResponse.toEntity(): ProductEntity{
    val finalPrice = priceCents?.div(100.0) ?: 0.0

    return ProductEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        price = finalPrice,
        category = this.category,
        stock = this.stock,
        imageUrl = this.imageUrl
    )
}

fun ProductEntity.toDomain(): Product? {
    if (category.isNullOrEmpty()) return null
    return Product(
        id = this.id,
        name = this.name,
        description = this.description.orEmpty(),
        price = this.price,
        category = this.category,
        stock = this.stock ?: 0,
        imageUrl = this.imageUrl
    )
}