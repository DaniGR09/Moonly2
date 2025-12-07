package com.moonly.app.data.remote

/**
 * Clase sellada para manejar estados de respuestas de red
 */
sealed class NetworkResult<out T> {
    /**
     * Respuesta exitosa con datos
     */
    data class Success<out T>(val data: T) : NetworkResult<T>()

    /**
     * Error con mensaje
     */
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()

    /**
     * Estado de carga
     */
    data object Loading : NetworkResult<Nothing>()
}

/**
 * Extensión para verificar si es éxito
 */
fun <T> NetworkResult<T>.isSuccess(): Boolean = this is NetworkResult.Success

/**
 * Extensión para verificar si es error
 */
fun <T> NetworkResult<T>.isError(): Boolean = this is NetworkResult.Error

/**
 * Extensión para verificar si está cargando
 */
fun <T> NetworkResult<T>.isLoading(): Boolean = this is NetworkResult.Loading

/**
 * Extensión para obtener datos o null
 */
fun <T> NetworkResult<T>.getDataOrNull(): T? = when (this) {
    is NetworkResult.Success -> data
    else -> null
}

/**
 * Extensión para obtener mensaje de error o null
 */
fun <T> NetworkResult<T>.getErrorMessageOrNull(): String? = when (this) {
    is NetworkResult.Error -> message
    else -> null
}