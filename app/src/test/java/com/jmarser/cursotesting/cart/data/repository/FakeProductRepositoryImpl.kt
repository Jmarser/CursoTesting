package com.jmarser.cursotesting.cart.data.repository

import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Project: CursoTesting
 * File: FakeProductRepositoryImpl.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 17/04/2026
 */

class FakeProductRepositoryImpl: ProductRepository {

    private val _products = MutableStateFlow<List<Product>>(emptyList())

    fun setProducts(products: List<Product>){
        _products.value = products
    }

    override fun getProducts(): Flow<List<Product>> = _products.asStateFlow()

    override fun getProductById(id: String): Flow<Product?> {
        return _products.asStateFlow().map { products ->
            products.find { it.id == id }
        }
    }

    override suspend fun refreshProduct() {
        // No effect
    }

    override fun getProductsByIds(ids: Set<String>): Flow<List<Product>> {
        return _products.asStateFlow().map { products ->
            products.filter { it.id in ids }
        }
    }
}