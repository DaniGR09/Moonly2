package com.moonly.app.data.repository

import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.api.CareMethodsApi
import com.moonly.app.data.remote.dto.care_methods.*
import com.moonly.app.data.remote.dto.common.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CareMethodsRepository @Inject constructor(
    private val careMethodsApi: CareMethodsApi,
    private val gson: Gson
) {

    /**
     * Crea un método de cuidado
     */
    fun createCareMethod(request: CareMethodCreateRequest): Flow<NetworkResult<CareMethodResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = careMethodsApi.createCareMethod(request)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al crear método de cuidado"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Obtiene todos los métodos de cuidado
     */
    fun getCareMethods(activeOnly: Boolean = true): Flow<NetworkResult<List<CareMethodResponse>>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = careMethodsApi.getCareMethods(activeOnly)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al obtener métodos de cuidado"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Obtiene un método de cuidado específico
     */
    fun getCareMethod(methodId: String): Flow<NetworkResult<CareMethodResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = careMethodsApi.getCareMethod(methodId)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al obtener método de cuidado"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Actualiza un método de cuidado
     */
    fun updateCareMethod(methodId: String, request: CareMethodUpdateRequest): Flow<NetworkResult<CareMethodResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = careMethodsApi.updateCareMethod(methodId, request)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al actualizar método de cuidado"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Registra un cambio de método
     */
    fun registerChange(methodId: String): Flow<NetworkResult<CareMethodResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val request = CareMethodChangeRequest(methodId)
            val response = careMethodsApi.registerChange(request)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al registrar cambio"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Elimina un método de cuidado
     */
    fun deleteCareMethod(methodId: String): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = careMethodsApi.deleteCareMethod(methodId)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!.message))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al eliminar método de cuidado"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }
}