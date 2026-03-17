package com.jmarser.cursotesting.productlist.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jmarser.cursotesting.productlist.data.local.database.entity.PromotionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Project: CursoTesting
 * File: PromotionDao.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 16/03/2026
 */

@Dao
interface PromotionDao {

    @Query("SELECT * FROM promotions")
    fun getAllPromotions(): Flow<List<PromotionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromotions(promotions: List<PromotionEntity>)

    @Query("DELETE FROM promotions")
    suspend fun clearPromotions()

    @Transaction
    suspend fun replaceAll(promotions: List<PromotionEntity>){
        clearPromotions()
        insertPromotions(promotions)
    }
}