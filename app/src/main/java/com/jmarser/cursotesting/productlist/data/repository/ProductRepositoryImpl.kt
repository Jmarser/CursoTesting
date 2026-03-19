package com.jmarser.cursotesting.productlist.data.repository

import com.jmarser.cursotesting.core.domain.coroutines.DispatchersProvider
import com.jmarser.cursotesting.productlist.data.local.LocalDataSource
import com.jmarser.cursotesting.productlist.data.mappers.toDomain
import com.jmarser.cursotesting.productlist.data.mappers.toEntity
import com.jmarser.cursotesting.productlist.data.remote.RemoteDataSource
import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext

/**
 * Project: CursoTesting
 * File: ProductRepositoryImpl.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 13/03/2026
 */

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatchers: DispatchersProvider
) : ProductRepository {

    private val refreshScope = CoroutineScope(SupervisorJob() + dispatchers.io)
    private val refreshMutex = Mutex()

    override fun getProducts(): Flow<List<Product>> {
        return localDataSource.getAllProducts().map { entities ->
            entities.mapNotNull { it.toDomain() }
        }.onStart {
            refreshScope.launch {
                if (!refreshMutex.tryLock()) return@launch
                try {
                    refreshProduct()
                }catch (e: Exception){

                }finally {
                    refreshMutex.unlock()
                }
            }
        }.catch {
            // Log por hacer
        }
    }

    override fun getProductById(id: String): Flow<Product?> {
        return localDataSource.getProductById(id)
            .map { entity ->  entity?.toDomain() }
            .catch { e -> }
    }

    override suspend fun refreshProduct() {
        withContext(dispatchers.io) {
            val products = remoteDataSource.getProducts().getOrThrow()

            val productsEntity = products.map { it.toEntity() }

            localDataSource.saveProducts(productsEntity)
        }
    }
}