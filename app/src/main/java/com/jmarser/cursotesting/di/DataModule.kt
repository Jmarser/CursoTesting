package com.jmarser.cursotesting.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.jmarser.cursotesting.cart.data.local.database.dao.CartDao
import com.jmarser.cursotesting.cart.data.repository.CartRepositoryImpl
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.checkout.data.repository.OrderRepositoryImpl
import com.jmarser.cursotesting.checkout.domain.repository.OrderRepository
import com.jmarser.cursotesting.core.data.local.database.MarketDatabase
import com.jmarser.cursotesting.core.data.util.ClockImpl
import com.jmarser.cursotesting.core.domain.util.Clock
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
    fun provideProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository {
        return productRepositoryImpl
    }

    @Provides
    @Singleton
    fun providePromotionRepository(promotionRepositoryImpl: PromotionRepositoryImpl): PromotionRepository {
        return promotionRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository {
        return cartRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository{
        return orderRepositoryImpl
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
    fun providesDatabase(@ApplicationContext context: Context): MarketDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = MarketDatabase::class.java,
            name = "market_database"
        ).fallbackToDestructiveMigration(dropAllTables = true)
         .build()
    }

    @Provides
    @Singleton
    fun providesCartDao(database: MarketDatabase): CartDao {
        return database.cartDao()
    }

    @Provides
    @Singleton
    fun provideClock(clock: ClockImpl): Clock{
        return clock
    }

}