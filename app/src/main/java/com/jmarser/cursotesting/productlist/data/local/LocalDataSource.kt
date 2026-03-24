package com.jmarser.cursotesting.productlist.data.local

import com.jmarser.cursotesting.cart.data.local.database.dao.CartDao
import com.jmarser.cursotesting.cart.data.local.database.entity.CartEntity
import com.jmarser.cursotesting.productlist.data.local.database.dao.ProductDao
import com.jmarser.cursotesting.productlist.data.local.database.dao.PromotionDao
import com.jmarser.cursotesting.productlist.data.local.database.entity.ProductEntity
import com.jmarser.cursotesting.productlist.data.local.database.entity.PromotionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: LocalDataSource.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 16/03/2026
 */

class LocalDataSource @Inject constructor(
    private val productDao: ProductDao,
    private val promotionDao: PromotionDao,
    private val cartDao: CartDao
) {

    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    suspend fun saveProducts(products: List<ProductEntity>) {
        productDao.replaceAll(products)
    }

    fun getProductById(productId: String): Flow<ProductEntity?> =
        productDao.getProductById(productId)

    fun getAllPromotions(): Flow<List<PromotionEntity>> = promotionDao.getAllPromotions()

    suspend fun savePromotions(promotions: List<PromotionEntity>) {
        promotionDao.replaceAll(promotions)
    }

    fun getAllCartItems(): Flow<List<CartEntity>> = cartDao.getAllCartItems()

    suspend fun getCartItemById(productId: String): CartEntity? = cartDao.getCartItemById(productId)

    suspend fun insertcartItem(item: CartEntity): Result<Unit>{
        return try {
            cartDao.insertCartItem(item)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun updateCartItem(cartItem: CartEntity): Result<Unit>{
        return try {
            cartDao.updateCartItem(cartItem)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun deleteCartItem(cartItem: CartEntity): Result<Unit>{
        return try {
            cartDao.deleteCartItem(cartItem)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun clearCart(): Result<Unit>{
        return try {
            cartDao.clearCart()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    fun getProductsByIds(productIds: Set<String>): Flow<List<ProductEntity>>{
        if (productIds.isEmpty()) return flowOf(emptyList())
        return productDao.getProductsIds(productIds.toList())
     }
}