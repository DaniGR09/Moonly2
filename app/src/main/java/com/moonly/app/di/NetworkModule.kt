package com.moonly.app.di

import com.google.gson.Gson
import com.moonly.app.core.constants.ApiConstants
import com.moonly.app.data.local.PreferenceManager
import com.moonly.app.data.local.datastore.PreferencesDataStore
import com.moonly.app.data.remote.api.*
import com.moonly.app.data.remote.interceptor.AuthInterceptor
import com.moonly.app.data.remote.interceptor.ErrorInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // ==================== OKHTTP ====================

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        errorInterceptor: ErrorInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(errorInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    // ==================== RETROFIT ====================

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // ==================== INTERCEPTORES ====================

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        preferenceManager: PreferenceManager   // <-- CORRECTO
    ): AuthInterceptor {
        return AuthInterceptor(preferenceManager)
    }

    @Provides
    @Singleton
    fun provideErrorInterceptor(): ErrorInterceptor {
        return ErrorInterceptor()
    }

    // ==================== APIS ====================

    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi =
        retrofit.create(ProfileApi::class.java)

    @Provides @Singleton
    fun provideCycleApi(retrofit: Retrofit): CycleApi =
        retrofit.create(CycleApi::class.java)

    @Provides @Singleton
    fun provideContraceptivesApi(retrofit: Retrofit): ContraceptivesApi =
        retrofit.create(ContraceptivesApi::class.java)

    @Provides @Singleton
    fun provideCareMethodsApi(retrofit: Retrofit): CareMethodsApi =
        retrofit.create(CareMethodsApi::class.java)

    @Provides @Singleton
    fun provideMedicalConditionsApi(retrofit: Retrofit): MedicalConditionsApi =
        retrofit.create(MedicalConditionsApi::class.java)

    @Provides @Singleton
    fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi =
        retrofit.create(NotificationsApi::class.java)
}
