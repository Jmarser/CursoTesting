package com.jmarser.cursotesting.cart.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jmarser.cursotesting.cart.data.local.database.entity.CartEntity
import com.jmarser.cursotesting.productlist.data.local.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Project: CursoTesting
 * File: CartDao.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 19/03/2026
 */

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItemById(productId: String): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartEntity)

    @Update
    suspend fun updateCartItem(cartItem: CartEntity)

    @Delete
    suspend fun deleteCartItem(cartItem: CartEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT * FROM products WHERE id IN (:productsIds)")
    fun getProductsByIds(productsIds: List<String>): Flow<List<ProductEntity>>
}