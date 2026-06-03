package com.jmarser.cursotesting.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.jmarser.cursotesting.di.DataStoreModule
import com.jmarser.cursotesting.productlist.data.repository.SettingsRepositoryImpl
import com.jmarser.cursotesting.productlist.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Singleton

private val Context.testingDataStore: DataStore<Preferences> by preferencesDataStore("testing_settings")

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
abstract class TestDataStoreModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository

    companion object{
        @Provides
        @Singleton
        fun provideDataStore(): DataStore<Preferences>{
            val context = ApplicationProvider.getApplicationContext<Context>()
/*            context.preferencesDataStoreFile("testing_settings").delete()
            return ApplicationProvider.getApplicationContext<Context>().testingDataStore*/
            return context.testingDataStore.apply {
                runBlocking { edit { preferences -> preferences.clear() } }
            }
        }
    }

}