package com.jmarser.cursotesting.di

import com.jmarser.cursotesting.BuildConfig
import com.jmarser.cursotesting.productlist.data.remote.MarketApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl(): String{
        return "https://raw.githubusercontent.com/ArisGuimera/minimarket-api/main/"
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient{
        val logginInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG){
            builder.addInterceptor(logginInterceptor )
        }

        return builder
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
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
        json: Json,
        @Named("baseUrl") baseUrl: String
    ): Retrofit {
        val contentType: MediaType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
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