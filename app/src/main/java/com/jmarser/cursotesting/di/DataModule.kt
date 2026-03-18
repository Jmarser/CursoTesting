package com.jmarser.cursotesting.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.jmarser.cursotesting.productlist.data.local.database.MarketDatabase
import com.jmarser.cursotesting.productlist.data.local.database.dao.ProductDao
import com.jmarser.cursotesting.productlist.data.local.database.dao.PromotionDao
import com.jmarser.cursotesting.productlist.data.repository.ProductRepositoryImpl
import com.jmarser.cursotesting.productlist.data.repository.PromotionRepositoryImpl
import com.jmarser.cursotesting.productlist.data.repository.SettingsRepositoryImpl
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
import com.jmarser.cursotesting.productlist.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository{
        return productRepositoryImpl
    }

    @Provides
    @Singleton
    fun providePromotionRepository(promotionRepositoryImpl: PromotionRepositoryImpl): PromotionRepository{
        return promotionRepositoryImpl
    }

    @Provides
    fun providesProductDao(database: MarketDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun providesPromotionDao(database: MarketDatabase): PromotionDao {
        return database.promotionDao()
    }

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): MarketDatabase{
        return Room.databaseBuilder(
            context = context,
            klass = MarketDatabase::class.java,
            name = "market_database"
        ).build()
    }

/*    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences>{
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository{
        return settingsRepositoryImpl
    }*/
}