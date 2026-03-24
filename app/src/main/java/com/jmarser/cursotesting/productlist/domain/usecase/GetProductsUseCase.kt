package com.jmarser.cursotesting.productlist.domain.usecase

import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
import com.jmarser.cursotesting.productlist.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Instant
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: GetProductsUseCase.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 16/03/2026
 */

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val promotionRepository: PromotionRepository,
    private val getPromotionForProduct: GetPromotionForProduct,
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<List<ProductWithPromotion>> {
        return combine(
            productRepository.getProducts(), promotionRepository.getActivePromotions(), settingsRepository.inStockOnly
        ) { products, promotions, inStockOnly ->

            val now = Instant.now()

            val activePromotion = promotions.filter {
                it.startTime <= now && it.endTime >= now
            }

            val filteredProducts = if (inStockOnly){
                products.filter { it.stock > 0 }
            }else{
                products
            }

            filteredProducts.map { product ->
                val promotion = getPromotionForProduct(product, activePromotion)
                ProductWithPromotion(product = product, promotion = promotion)
            }
        }

    }
}