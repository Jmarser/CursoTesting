package com.jmarser.cursotesting.productlist.data.remote

import com.jmarser.cursotesting.core.domain.model.AppError
import com.jmarser.cursotesting.productlist.data.remote.model.ProductResponse
import com.jmarser.cursotesting.productlist.data.remote.model.PromotionResponse
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: RemoteDataSource.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 13/03/2026
 */

class RemoteDataSource @Inject constructor(
    val marketApiService: MarketApiService
) {

    suspend fun getProducts(): Result<List<ProductResponse>>{

        return try {
            val response = marketApiService.getProducts()
            Result.success(response.products)

        }catch (e: Exception){
            Result.failure(mapToDomainError(e))
        }
    }

    suspend fun getPromotions(): Result<List<PromotionResponse>>{
        return try {
            val response = marketApiService.getPromotions()
            Result.success(response.promotions)

        }catch (e: Exception){
            Result.failure(mapToDomainError(e))
        }
    }

    private fun mapToDomainError(e: Exception): AppError{
        return when(e){
            is UnknownHostException -> AppError.NetworkError
            is SocketTimeoutException -> AppError.NetworkError
            is IOException -> AppError.NetworkError
            is HttpException -> {
                when(e.code()){
                    404 -> AppError.NotFoundError
                    else -> AppError.NetworkError
                }
            }
            else -> AppError.UnknowError(e.message)
        }
    }
}