package com.jmarser.cursotesting.productlist.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jmarser.cursotesting.productlist.data.local.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Project: CursoTesting
 * File: ProductDao.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 16/03/2026
 */

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: String): Flow<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clearProducts()

    @Transaction
    suspend fun replaceAll(products: List<ProductEntity>){
        clearProducts()
        insertAllProducts(products)
    }

    @Query("SELECT * FROM products WHERE id IN (:productsIds)")
    fun getProductsIds(productsIds: List<String>): Flow<List<ProductEntity>>
}