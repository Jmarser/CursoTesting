package com.jmarser.cursotesting.productlist.data.mappers

import com.jmarser.cursotesting.productlist.data.local.database.entity.PromotionEntity
import com.jmarser.cursotesting.productlist.data.remote.model.PromotionResponse
import com.jmarser.cursotesting.productlist.domain.model.Promotion
import com.jmarser.cursotesting.productlist.domain.model.PromotionType
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.time.Instant


/**
 * Project: CursoTesting
 * File: PromotionMapper.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 17/03/2026
 */

fun PromotionResponse.toEntity(json: Json): PromotionEntity?{

    if (startAtEpoch == null || endAtEpoch == null) return null

    val productIds: List<String> = listOf(productId)
    val productIdsJson: String = json.encodeToString(
        serializer = ListSerializer(String.serializer()),
        value = productIds
    )

    return PromotionEntity(
        id = this.id,
        productIds = productIdsJson,
        type = this.type,
        percent = this.percent,
        buyX = this.buyX,
        payY = this.payY,
        startAtEpoch = this.startAtEpoch,
        endAtEpoch = this.endAtEpoch
    )
}

fun PromotionEntity.toDomain(json: Json): Promotion? {

    val decodeProductIds = runCatching {
        json.decodeFromString(
            ListSerializer(String.serializer()),
            productIds
        )
    }.getOrNull()

    val finalType = runCatching {
        PromotionType.valueOf(type.trim().uppercase())
    }.getOrNull()

    if (finalType == null || decodeProductIds == null) return null

    val finalPromotionValue: Double? = when(finalType){
        PromotionType.PERCENT -> percent
        PromotionType.BUY_X_PAY_Y -> payY
    }?.toDouble()

    finalPromotionValue ?: return null

    return Promotion(
        id = this.id,
        productIds = decodeProductIds,
        type = finalType,
        value = finalPromotionValue,
        buyQuantity = this.buyX,
        startTime = Instant.ofEpochSecond(startAtEpoch),
        endTime = Instant.ofEpochSecond(endAtEpoch)
    )
}