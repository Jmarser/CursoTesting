package com.jmarser.cursotesting.productlist.data.remote

import com.jmarser.cursotesting.checkout.data.remote.response.OrderConfirmationResponse
import com.jmarser.cursotesting.productlist.data.remote.model.ProductsResponse
import com.jmarser.cursotesting.productlist.data.remote.model.PromotionsResponse
import retrofit2.http.GET

/**
 * Project: CursoTesting
 * File: MarketApiService.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 13/03/2026
 */

interface MarketApiService {

    @GET("data/products.json")
    suspend fun getProducts(): ProductsResponse

    @GET("data/promotions.json")
    suspend fun getPromotions(): PromotionsResponse

    @GET("data/order_confirmation.json")
    suspend fun placeOrder(): OrderConfirmationResponse
}