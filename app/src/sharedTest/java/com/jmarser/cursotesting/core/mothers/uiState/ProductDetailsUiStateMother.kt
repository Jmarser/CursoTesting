package com.jmarser.cursotesting.core.mothers.uiState

import com.jmarser.cursotesting.ProductDetail.presentation.ProductDetailUiState
import com.jmarser.cursotesting.core.mothers.ProductMother
import com.jmarser.cursotesting.core.mothers.PromotionMother
import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion

object ProductDetailsUiStateMother {
    fun ProductDetailsWithoutPromotion(
    ) = ProductDetailUiState(
        item = ProductWithPromotion(
            product = ProductMother.bread(4),
            promotion = null
        ),
        isLoading = false
    )

    fun isLoading() = ProductDetailUiState(
        item = null,
        isLoading = true
    )

    fun ProductDetailsWithPromotionPercent(
    ) = ProductDetailUiState(
        item = ProductWithPromotion(
            product = ProductMother.bread(4),
            promotion = PromotionMother.percent()
        ),
        isLoading = false
    )

    fun ProductDetailsWithPromotionBuyPay(
    ) = ProductDetailUiState(
        item = ProductWithPromotion(
            product = ProductMother.bread(4),
            promotion = PromotionMother.buyXpayY()
        ),
        isLoading = false
    )

    fun ProductDetailsWithoutPromotionWithoutDescription(
    ) = ProductDetailUiState(
        item = ProductWithPromotion(
            product = ProductMother.cola(6),
            promotion = null
        ),
        isLoading = false
    )

    fun ProductDetailsWithoutPromotionWithoutStock(
    ) = ProductDetailUiState(
        item = ProductWithPromotion(
            product = ProductMother.milk(0),
            promotion = null
        ),
        isLoading = false
    )
}