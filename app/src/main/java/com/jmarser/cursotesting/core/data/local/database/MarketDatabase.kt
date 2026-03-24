package com.jmarser.cursotesting.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jmarser.cursotesting.cart.data.local.database.dao.CartDao
import com.jmarser.cursotesting.cart.data.local.database.entity.CartEntity
import com.jmarser.cursotesting.productlist.data.local.database.dao.ProductDao
import com.jmarser.cursotesting.productlist.data.local.database.dao.PromotionDao
import com.jmarser.cursotesting.productlist.data.local.database.entity.ProductEntity
import com.jmarser.cursotesting.productlist.data.local.database.entity.PromotionEntity

/**
 * Project: CursoTesting
 * File: MarketDatabase.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 16/03/2026
 */

@Database(
    entities = [ProductEntity::class, PromotionEntity::class, CartEntity::class],
    version = 1,
    exportSchema = true
)
abstract class MarketDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun promotionDao(): PromotionDao
    abstract fun cartDao(): CartDao

}