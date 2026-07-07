package com.jmarser.cursotesting.core.builders

import com.jmarser.cursotesting.productlist.domain.model.Product

/**
 * Project: CursoTesting
 * File: ProductBuilder.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 26/04/2026
 */

class ProductBuilder {
    private var id: String = "product-1"
    private var name: String = "Producto de pruebas"
    private var description: String = ""
    private var price: Double = 10.0
    private var category: String = "Test categoty"
    private var stock: Int = 10
    private var imageUrl: String? = null

    fun build() = Product(
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

fun product(block: ProductBuilder.() -> Unit = {}) = ProductBuilder().apply(block).build()