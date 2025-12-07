package com.moonly.app.data.repository

import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.api.MedicalConditionsApi
import com.moonly.app.data.remote.dto.common.ErrorResponse
import com.moonly.app.data.remote.dto.medical.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicalConditionsRepository @Inject constructor(
    private val medicalConditionsApi: MedicalConditionsApi,
    private val gson: Gson
) {

    /**
     * Crea una condición médica
     */
    fun createMedicalCondition(request: MedicalConditionCreateRequest): Flow<NetworkResult<MedicalConditionResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = medicalConditionsApi.createMedicalCondition(request)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al crear condición médica"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Obtiene todas las condiciones médicas
     */
    fun getMedicalConditions(activeOnly: Boolean = true): Flow<NetworkResult<List<MedicalConditionResponse>>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = medicalConditionsApi.getMedicalConditions(activeOnly)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al obtener condiciones médicas"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Obtiene una condición médica específica
     */
    fun getMedicalCondition(conditionId: String): Flow<NetworkResult<MedicalConditionResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = medicalConditionsApi.getMedicalCondition(conditionId)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al obtener condición médica"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Actualiza una condición médica
     */
    fun updateMedicalCondition(conditionId: String, request: MedicalConditionUpdateRequest): Flow<NetworkResult<MedicalConditionResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = medicalConditionsApi.updateMedicalCondition(conditionId, request)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al actualizar condición médica"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Elimina una condición médica
     */
    fun deleteMedicalCondition(conditionId: String): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = medicalConditionsApi.deleteMedicalCondition(conditionId)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!.message))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al eliminar condición médica"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }
}