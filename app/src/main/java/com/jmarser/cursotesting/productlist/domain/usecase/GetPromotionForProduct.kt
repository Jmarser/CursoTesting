package com.jmarser.cursotesting.productlist.domain.usecase

import com.jmarser.cursotesting.core.utils.roundTo2Decimals
import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.model.ProductPromotion
import com.jmarser.cursotesting.productlist.domain.model.Promotion
import com.jmarser.cursotesting.productlist.domain.model.PromotionType
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: getPromotionForProduct.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 17/03/2026
 */

class GetPromotionForProduct @Inject constructor() {

    operator fun invoke(product: Product, promotions: List<Promotion>): ProductPromotion?{
        val productPromos = promotions.filter { it.productIds.contains(product.id) }

        val percentPromo = productPromos.filter { it.type == PromotionType.PERCENT }
            .maxByOrNull { it.value }

        if (percentPromo != null){
            val percent = percentPromo.value.coerceIn(0.0, 100.0)
            val discountPrice = (product.price * (1 - percent / 100.0)).roundTo2Decimals()
            return ProductPromotion.Percent(percent = percent, discountedPrice = discountPrice)
        }

        val buyPayPromo = productPromos.firstOrNull(){ it.type == PromotionType.BUY_X_PAY_Y }
        if (buyPayPromo != null){
            val buy = buyPayPromo.buyQuantity ?: return null
            val pay = buyPayPromo.value.toInt().coerceIn(0, buy)
            val price = ((pay * product.price) / buy).roundTo2Decimals()

            return ProductPromotion.BuyXPayY(
                buy = buy,
                pay = pay,
                label = "${buy}x${pay}",
                unitPrice = price
            )
        }
        return null
    }
}