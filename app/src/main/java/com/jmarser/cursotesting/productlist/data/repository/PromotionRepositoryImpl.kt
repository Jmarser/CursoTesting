package com.jmarser.cursotesting.productlist.data.repository

import com.jmarser.cursotesting.core.domain.coroutines.DispatchersProvider
import com.jmarser.cursotesting.productlist.data.local.LocalDataSource
import com.jmarser.cursotesting.productlist.data.local.database.entity.PromotionEntity
import com.jmarser.cursotesting.productlist.data.mappers.toDomain
import com.jmarser.cursotesting.productlist.data.mappers.toEntity
import com.jmarser.cursotesting.productlist.data.remote.RemoteDataSource
import com.jmarser.cursotesting.productlist.domain.model.Promotion
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: PromotionRepositoryImpl.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 17/03/2026
 */

class PromotionRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatchers: DispatchersProvider,
    private val json: Json
): PromotionRepository {

    private val refreshScope = CoroutineScope(SupervisorJob() + dispatchers.io)
    private val refreshMutex = Mutex()

    override fun getActivePromotions(): Flow<List<Promotion>> {
        return localDataSource.getAllPromotions().map { entities ->
            entities.mapNotNull { it.toDomain(json) }
        }.onStart {
            refreshScope.launch {
                if (!refreshMutex.tryLock()) return@launch
                try {
                    refreshPromotions()
                }catch (e: Exception){

                }finally {
                    refreshMutex.unlock()
                }
            }
        }.catch {
            // Log por hacer
        }
    }

    override suspend fun refreshPromotions() {
        withContext(dispatchers.io){
            val promotions = remoteDataSource.getPromotions().getOrThrow()
            val promotionsEntity: List<PromotionEntity> = promotions.mapNotNull { it.toEntity(json) }

            localDataSource.savePromotions(promotionsEntity)
        }
    }
}