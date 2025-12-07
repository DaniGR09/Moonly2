package com.moonly.app.data.remote.interceptor

import com.moonly.app.data.local.PreferenceManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor que agrega el token de autenticación a todas las peticiones.
 */
class AuthInterceptor(
    private val preferenceManager: PreferenceManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Obtener token de forma síncrona
        val token = runBlocking {
            try {
                preferenceManager.accessToken.first()
            } catch (e: Exception) {
                null
            }
        }

        // Si no hay token, continuar sin modificar (para endpoints públicos como login)
        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        // Agregar header de autorización con el formato Bearer
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}