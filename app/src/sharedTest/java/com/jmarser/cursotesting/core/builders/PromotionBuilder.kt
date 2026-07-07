package com.jmarser.cursotesting.core.builders

import com.jmarser.cursotesting.productlist.domain.model.Promotion
import com.jmarser.cursotesting.productlist.domain.model.PromotionType
import java.time.Instant

/**
 * Project: CursoTesting
 * File: PromotionBuilder.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 27/04/2026
 */

class PromotionBuilder {

    private var id: String = "promotion-1"
    private var type: PromotionType = PromotionType.PERCENT
    private var productIds: List<String> = listOf("product-1")
    private var value: Double = 10.0
    private var buyQuantity: Int? = null
    private var startTime: Instant = Instant.now().minusSeconds(3600)
    private var endTime: Instant = Instant.now().plusSeconds(3600)

    fun withId(id: String) = apply { this.id = id }
    fun withType(type: PromotionType) = apply { this.type = type }
    fun withProductIds(productIds: List<String>) = apply { this.productIds = productIds }
    fun withValue(value: Double) = apply { this.value = value }
    fun withBuyQuantity(buyQuantity: Int?) = apply { this.buyQuantity = buyQuantity }
    fun withStartTime(startTime: Instant) = apply { this.startTime = startTime }
    fun withEndTime(endTime: Instant) = apply { this.endTime = endTime }

    fun build() = Promotion(
        id = id,
        type = type,
        productIds = productIds,
        value = value,
        buyQuantity = buyQuantity,
        startTime = startTime,
        endTime = endTime
    )
}

fun promotion(block: PromotionBuilder.() -> Unit = {}) = PromotionBuilder().apply(block).build()