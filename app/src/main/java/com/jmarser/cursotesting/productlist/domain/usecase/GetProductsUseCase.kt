package com.jmarser.cursotesting.productlist.domain.usecase

import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: GetProductsUseCase.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 16/03/2026
 */

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    operator fun invoke(): Flow<List<Product>>{
        return productRepository.getProducts()
    }
}