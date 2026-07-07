package com.jmarser.cursotesting.checkout.data.repository

import com.jmarser.cursotesting.checkout.data.mapper.toDomain
import com.jmarser.cursotesting.checkout.domain.model.OrderConfirmation
import com.jmarser.cursotesting.checkout.domain.repository.OrderRepository
import com.jmarser.cursotesting.productlist.data.remote.RemoteDataSource
import jakarta.inject.Inject

/**
 * Project: CursoTesting
 * File: OrderRepositoryImpl.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 07/07/2026
 */

class OrderRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): OrderRepository {
    override suspend fun placeOrder(): OrderConfirmation {
        return remoteDataSource.placeOrder().getOrThrow().toDomain()
    }
}