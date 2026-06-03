package com.jmarser.cursotesting.core.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.jmarser.cursotesting.cart.data.local.database.dao.CartDao
import com.jmarser.cursotesting.cart.data.repository.CartRepositoryImpl
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.core.data.local.database.MarketDatabase
import com.jmarser.cursotesting.core.data.util.ClockImpl
import com.jmarser.cursotesting.core.domain.util.Clock
import com.jmarser.cursotesting.di.DataModule
import com.jmarser.cursotesting.productlist.data.local.database.dao.ProductDao
import com.jmarser.cursotesting.productlist.data.local.database.dao.PromotionDao
import com.jmarser.cursotesting.productlist.data.repository.ProductRepositoryImpl
import com.jmarser.cursotesting.productlist.data.repository.PromotionRepositoryImpl
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
object TestDataModule {
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
    fun providesProductDao(database: MarketDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun providesPromotionDao(database: MarketDatabase): PromotionDao {
        return database.promotionDao()
    }

    @Provides
    @Singleton
    fun providesDatabase(): MarketDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(
            context = context,
            klass = MarketDatabase::class.java
        ).build()
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