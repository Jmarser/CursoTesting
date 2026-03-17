package com.jmarser.cursotesting.productlist.domain.repository

import com.jmarser.cursotesting.productlist.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Project: CursoTesting
 * File: ProductRepository.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 13/03/2026
 */

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProductById(id: String): Flow<Product?>
    suspend fun refreshProduct()
}