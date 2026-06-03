package com.jmarser.cursotesting.core.di


import com.jmarser.cursotesting.core.mockwebserver.MockWebServerUrlHolder
import com.jmarser.cursotesting.di.NetworkModule
import com.jmarser.cursotesting.productlist.data.remote.MarketApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Project: CursoTesting
 * File: TestNetworkModule.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 31/05/2026
 */

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object TestNetworkModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient{

        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideJson(): Json{
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType: MediaType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(MockWebServerUrlHolder.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

    }

    @Provides
    @Singleton
    fun provideMarketApiService(retrofit: Retrofit): MarketApiService{
        return retrofit.create(MarketApiService::class.java)
    }
}