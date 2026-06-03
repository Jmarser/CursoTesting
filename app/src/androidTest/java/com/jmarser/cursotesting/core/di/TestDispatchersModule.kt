package com.jmarser.cursotesting.core.di

import com.jmarser.cursotesting.core.data.coroutines.DispatchersProviderImpl
import com.jmarser.cursotesting.core.domain.coroutines.DispatchersProvider
import com.jmarser.cursotesting.di.DispatchersModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class]
)
abstract class TestDispatchersModule {

    @Binds
    @Singleton
    abstract fun bindDispatchersProvider(dispatchersProviderImpl: DispatchersProviderImpl): DispatchersProvider
}