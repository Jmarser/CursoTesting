package com.jmarser.cursotesting.core.builder

import com.jmarser.cursotesting.productlist.data.local.database.entity.ProductEntity

/**
 * Project: CursoTesting
 * File: ProductEntityBuilder.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 18/05/2026
 */

class ProductEntityBuilder {
    private var id: String = "product-1"
    private var name: String = "Producto de pruebas"
    private var description: String = "Descripción completa"
    private var price: Double = 10.0
    private var category: String = "Test categoty"
    private var stock: Int = 10
    private var imageUrl: String? = null

    fun build() = ProductEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        category = category,
        stock = stock,
        imageUrl = imageUrl
    )

    fun withId(id: String) = apply { this.id = id }
    fun withName(name: String) = apply { this.name = name }
    fun withDescription(description: String) = apply { this.description = description }
    fun withPrice(price: Double) = apply { this.price = price }
    fun withCategory(category: String) = apply { this.category = category }
    fun withStock(stock: Int) = apply { this.stock = stock }
    fun withImageUrl(imageUrl: String) = apply { this.imageUrl = imageUrl }
}

fun productEntity(block: ProductEntityBuilder.() -> Unit = {}) = ProductEntityBuilder().apply(block).build()