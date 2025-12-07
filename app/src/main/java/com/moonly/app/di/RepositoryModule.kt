package com.moonly.app.di

import com.google.gson.Gson
import com.moonly.app.data.local.datastore.PreferencesDataStore
import com.moonly.app.data.remote.api.*
import com.moonly.app.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Repositories
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        preferencesDataStore: PreferencesDataStore,
        gson: Gson
    ): AuthRepository {
        return AuthRepository(authApi, preferencesDataStore, gson)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        profileApi: ProfileApi,
        preferencesDataStore: PreferencesDataStore,
        gson: Gson
    ): ProfileRepository {
        return ProfileRepository(profileApi, preferencesDataStore, gson)
    }

    @Provides
    @Singleton
    fun provideCycleRepository(
        cycleApi: CycleApi,
        gson: Gson
    ): CycleRepository {
        return CycleRepository(cycleApi, gson)
    }

    @Provides
    @Singleton
    fun provideContraceptivesRepository(
        contraceptivesApi: ContraceptivesApi,
        gson: Gson
    ): ContraceptivesRepository {
        return ContraceptivesRepository(contraceptivesApi, gson)
    }

    @Provides
    @Singleton
    fun provideCareMethodsRepository(
        careMethodsApi: CareMethodsApi,
        gson: Gson
    ): CareMethodsRepository {
        return CareMethodsRepository(careMethodsApi, gson)
    }

    @Provides
    @Singleton
    fun provideMedicalConditionsRepository(
        medicalConditionsApi: MedicalConditionsApi,
        gson: Gson
    ): MedicalConditionsRepository {
        return MedicalConditionsRepository(medicalConditionsApi, gson)
    }

    @Provides
    @Singleton
    fun provideNotificationsRepository(
        notificationsApi: NotificationsApi,
        gson: Gson
    ): NotificationsRepository {
        return NotificationsRepository(notificationsApi, gson)
    }
}