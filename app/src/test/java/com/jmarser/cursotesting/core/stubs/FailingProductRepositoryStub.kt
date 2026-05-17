package com.jmarser.cursotesting.core.stubs

import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * Project: CursoTesting
 * File: FailingProductRepositoryStub.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 12/05/2026
 */

class FailingProductRepositoryStub(private val exception: Throwable): ProductRepository {
    override fun getProducts(): Flow<List<Product>> = flow { throw exception  }

    override fun getProductById(id: String): Flow<Product?> = flowOf()

    override suspend fun refreshProduct() {}

    override fun getProductsByIds(ids: Set<String>): Flow<List<Product>> = flowOf()
}