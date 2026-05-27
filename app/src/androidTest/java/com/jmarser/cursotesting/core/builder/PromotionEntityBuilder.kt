package com.jmarser.cursotesting.core.builder

import com.jmarser.cursotesting.productlist.data.local.database.entity.PromotionEntity

/**
 * Project: CursoTesting
 * File: PromotionEntityBuilder.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 24/05/2026
 */

class PromotionEntityBuilder {
    private var id: String = "product-1"
    private var type: String = "PERCENT"
    private var productIds: String = """["product-1"]"""
    private var buyX: Int? = null
    private var payY: Int? = null
    private var percent: Int? = null
    private var startAtEpoch: Long = 1700000000L
    private var endAtEpoch: Long = 1800000000L

    fun build() = PromotionEntity(
        id = id,
        type = type,
        productIds = productIds,
        buyX = buyX,
        payY = payY,
        percent = percent,
        startAtEpoch = startAtEpoch,
        endAtEpoch = endAtEpoch
    )

    fun withId(id: String) = apply { this.id = id }
    fun withType(type: String) = apply { this.type = type }
    fun withProductIds(productIds: String) = apply { this.productIds = productIds }
    fun withBuyX(buyX: Int?) = apply { this.buyX = buyX }
    fun withPayY(payY: Int?) = apply { this.payY = payY }
    fun withPercent(percent: Int?) = apply { this.percent = percent }
    fun withStartAtEpoch(startAtEpoch: Long) = apply { this.startAtEpoch = startAtEpoch }
    fun withEndAtEpoch(endAtEpoch: Long) = apply { this.endAtEpoch = endAtEpoch }
}

fun promotionEntity(block: PromotionEntityBuilder.() -> Unit = {}) =
    PromotionEntityBuilder().apply(block).build()