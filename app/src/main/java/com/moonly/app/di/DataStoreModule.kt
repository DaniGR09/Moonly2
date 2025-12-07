package com.moonly.app.di

import android.content.Context
import com.moonly.app.data.local.datastore.PreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de DataStore
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /**
     * Provee PreferencesDataStore
     */
    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): PreferencesDataStore {
        return PreferencesDataStore(context)
    }
}