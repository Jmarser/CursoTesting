package com.jmarser.cursotesting.ProductDetail.domain.usecase

import com.jmarser.cursotesting.core.domain.util.Clock
import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
import com.jmarser.cursotesting.productlist.domain.usecase.GetPromotionForProduct
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Instant

/**
 * Project: CursoTesting
 * File: GetProductDetailWithPromotionUseCase.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 19/03/2026
 */

class GetProductDetailWithPromotionUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val promotionRepository: PromotionRepository,
    private val getPromotionForProduct: GetPromotionForProduct,
    private val clock: Clock
) {

    operator fun invoke(productId: String): Flow<ProductWithPromotion?> {
        return combine(
            productRepository.getProductById(productId),
            promotionRepository.getActivePromotions()
        ) {product, promotions ->
            val now = clock.now()

            val activePromotions = promotions.filter {
                it.startTime <= now && it.endTime >= now
            }

            product?.let {
                val finalPromotion = getPromotionForProduct(it, activePromotions)
                ProductWithPromotion(product = it, promotion = finalPromotion)

            }
        }
    }
}