package com.moonly.app.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Interceptor para manejar errores HTTP
 */
class ErrorInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return try {
            val response = chain.proceed(request)

            // Si la respuesta no es exitosa, lanzar excepción personalizada
            if (!response.isSuccessful) {
                throw HttpException(response.code, response.message)
            }

            response
        } catch (e: IOException) {
            throw NetworkException("Error de red: No se pudo conectar al servidor", e)
        }
    }
}

/**
 * Excepción personalizada para errores HTTP
 */
class HttpException(val code: Int, message: String) : IOException(message)

/**
 * Excepción personalizada para errores de red
 */
class NetworkException(message: String, cause: Throwable? = null) : IOException(message, cause)