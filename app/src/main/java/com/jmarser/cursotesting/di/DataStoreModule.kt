package com.jmarser.cursotesting.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.jmarser.cursotesting.productlist.data.repository.SettingsRepositoryImpl
import com.jmarser.cursotesting.productlist.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Project: CursoTesting
 * File: DataStoreModule.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 18/03/2026
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository

    companion object{
        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences>{
            return PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("settings")}
            )
        }
    }
}