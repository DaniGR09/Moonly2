package com.moonly.app.data.repository

import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.api.ContraceptivesApi
import com.moonly.app.data.remote.dto.common.ErrorResponse
import com.moonly.app.data.remote.dto.contraceptives.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContraceptivesRepository @Inject constructor(
    private val contraceptivesApi: ContraceptivesApi,
    private val gson: Gson
) {

    /**
     * Crea un anticonceptivo
     */
    fun createContraceptive(request: ContraceptiveCreateRequest): Flow<NetworkResult<ContraceptiveResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = contraceptivesApi.createContraceptive(request)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al crear anticonceptivo"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Obtiene todos los anticonceptivos
     */
    fun getContraceptives(activeOnly: Boolean = true): Flow<NetworkResult<List<ContraceptiveResponse>>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = contraceptivesApi.getContraceptives(activeOnly)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al obtener anticonceptivos"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Obtiene un anticonceptivo específico
     */
    fun getContraceptive(contraceptiveId: String): Flow<NetworkResult<ContraceptiveResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = contraceptivesApi.getContraceptive(contraceptiveId)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al obtener anticonceptivo"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Actualiza un anticonceptivo
     */
    fun updateContraceptive(contraceptiveId: String, request: ContraceptiveUpdateRequest): Flow<NetworkResult<ContraceptiveResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = contraceptivesApi.updateContraceptive(contraceptiveId, request)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al actualizar anticonceptivo"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Elimina un anticonceptivo
     */
    fun deleteContraceptive(contraceptiveId: String): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = contraceptivesApi.deleteContraceptive(contraceptiveId)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!.message))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al eliminar anticonceptivo"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }
}