package com.moonly.app.data.repository

import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.api.NotificationsApi
import com.moonly.app.data.remote.dto.common.ErrorResponse
import com.moonly.app.data.remote.dto.notifications.NotificationHistoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationsRepository @Inject constructor(
    private val notificationsApi: NotificationsApi,
    private val gson: Gson
) {

    /**
     * Obtiene el historial de notificaciones
     */
    fun getNotificationsHistory(page: Int = 1, pageSize: Int = 20): Flow<NetworkResult<NotificationHistoryResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = notificationsApi.getNotificationsHistory(page, pageSize)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al obtener historial de notificaciones"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Procesa notificaciones pendientes
     */
    fun processPendingNotifications(): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = notificationsApi.processPendingNotifications()

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!.message))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al procesar notificaciones"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Envía una notificación de prueba
     */
    fun sendTestNotification(title: String, message: String): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = notificationsApi.sendTestNotification(title, message)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!.message))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al enviar notificación de prueba"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }
}
