package com.jmarser.cursotesting.di

import com.jmarser.cursotesting.core.data.coroutines.DispatchersProviderImpl
import com.jmarser.cursotesting.core.domain.coroutines.DispatchersProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatchersModule {

    @Binds
    @Singleton
    abstract fun bindDispatchersProvider(dispatchersProviderImpl: DispatchersProviderImpl): DispatchersProvider
}