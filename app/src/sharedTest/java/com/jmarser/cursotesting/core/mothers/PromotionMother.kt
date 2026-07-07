package com.jmarser.cursotesting.core.mothers

import com.jmarser.cursotesting.productlist.domain.model.ProductPromotion

object PromotionMother {

    fun percent(
        percent: Double = 25.0,
        discountedPrice: Double = 4.65
    ) = ProductPromotion.Percent(
        percent = percent,
        discountedPrice = discountedPrice
    )

    fun buyXpayY(
        buy: Int = 3,
        pay: Int = 2,
        unitPrice: Double = 2.0
    ) = ProductPromotion.BuyXPayY(
        buy = buy,
        pay = pay,
        label = "3 x 2",
        unitPrice = unitPrice
    )
}