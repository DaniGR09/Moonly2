package com.moonly.app.data.remote

import com.moonly.app.core.constants.ApiConstants
import com.moonly.app.data.local.PreferenceManager
import com.moonly.app.data.remote.api.*
import com.moonly.app.data.remote.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente de Retrofit configurado para la API de Moonly.
 * Incluye autenticación automática y logging.
 */
object RetrofitClient {

    /**
     * Crea una instancia de Retrofit con autenticación.
     */
    fun create(preferenceManager: PreferenceManager): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(createOkHttpClient(preferenceManager))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Configura OkHttpClient con interceptores.
     */
    private fun createOkHttpClient(preferenceManager: PreferenceManager): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(preferenceManager)) // 🔑 Token automático
            .addInterceptor(createLoggingInterceptor())
            .build()
    }

    /**
     * Crea interceptor para logging (solo en DEBUG).
     */
    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // ==================== API INSTANCES ====================

    fun provideAuthApi(preferenceManager: PreferenceManager): AuthApi {
        return create(preferenceManager).create(AuthApi::class.java)
    }

    fun provideProfileApi(preferenceManager: PreferenceManager): ProfileApi {
        return create(preferenceManager).create(ProfileApi::class.java)
    }

    fun provideCycleApi(preferenceManager: PreferenceManager): CycleApi {
        return create(preferenceManager).create(CycleApi::class.java)
    }

    fun provideContraceptivesApi(preferenceManager: PreferenceManager): ContraceptivesApi {
        return create(preferenceManager).create(ContraceptivesApi::class.java)
    }

    fun provideCareMethodsApi(preferenceManager: PreferenceManager): CareMethodsApi {
        return create(preferenceManager).create(CareMethodsApi::class.java)
    }

    fun provideMedicalConditionsApi(preferenceManager: PreferenceManager): MedicalConditionsApi {
        return create(preferenceManager).create(MedicalConditionsApi::class.java)
    }

    fun provideNotificationsApi(preferenceManager: PreferenceManager): NotificationsApi {
        return create(preferenceManager).create(NotificationsApi::class.java)
    }
}