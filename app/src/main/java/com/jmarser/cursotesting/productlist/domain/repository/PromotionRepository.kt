package com.jmarser.cursotesting.productlist.domain.repository

import com.jmarser.cursotesting.productlist.domain.model.Promotion
import kotlinx.coroutines.flow.Flow

/**
 * Project: CursoTesting
 * File: PromotionRepository.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 17/03/2026
 */

interface PromotionRepository {

    fun getActivePromotions(): Flow<List<Promotion>>
    suspend fun refreshPromotions()
}