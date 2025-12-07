package com.moonly.app.data.repository

import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.api.CycleApi
import com.moonly.app.data.remote.dto.common.ErrorResponse
import com.moonly.app.data.remote.dto.cycle.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CycleRepository @Inject constructor(
    private val cycleApi: CycleApi,
    private val gson: Gson
) {

    // ==================== CYCLES ====================

    /**
     * Crea un nuevo ciclo
     */
    fun createCycle(cycleStartDate: String, cycleEndDate: String? = null): Flow<NetworkResult<CycleResponse>> = flow {
        emit(NetworkResult.Loading)

        val request = CycleCreateRequest(cycleStartDate, cycleEndDate)
        val response = cycleApi.createCycle(request)

        if (response.isSuccessful && response.body() != null) {
            emit(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                gson.fromJson(errorBody, ErrorResponse::class.java).detail
            } catch (e: Exception) {
                "Error al crear ciclo"
            }
            emit(NetworkResult.Error(errorMessage, response.code()))
        }
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Error desconocido"))
    }

    /**
     * Obtiene los ciclos del usuario
     */
    fun getCycles(limit: Int = 10): Flow<NetworkResult<List<CycleResponse>>> = flow {
        emit(NetworkResult.Loading)

        val response = cycleApi.getCycles(limit)

        if (response.isSuccessful && response.body() != null) {
            emit(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                gson.fromJson(errorBody, ErrorResponse::class.java).detail
            } catch (e: Exception) {
                "Error al obtener ciclos"
            }
            emit(NetworkResult.Error(errorMessage, response.code()))
        }
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Error desconocido"))
    }

    /**
     * Obtiene el ciclo actual
     */
    fun getCurrentCycle(): Flow<NetworkResult<CycleResponse>> = flow {
        emit(NetworkResult.Loading)

        val response = cycleApi.getCurrentCycle()

        if (response.isSuccessful && response.body() != null) {
            emit(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                gson.fromJson(errorBody, ErrorResponse::class.java).detail
            } catch (e: Exception) {
                "Error al obtener ciclo actual"
            }
            emit(NetworkResult.Error(errorMessage, response.code()))
        }
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Error desconocido"))
    }

    /**
     * Actualiza un ciclo
     */
    fun updateCycle(cycleId: String, cycleEndDate: String): Flow<NetworkResult<CycleResponse>> = flow {
        emit(NetworkResult.Loading)

        val request = CycleUpdateRequest(cycleEndDate)
        val response = cycleApi.updateCycle(cycleId, request)

        if (response.isSuccessful && response.body() != null) {
            emit(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                gson.fromJson(errorBody, ErrorResponse::class.java).detail
            } catch (e: Exception) {
                "Error al actualizar ciclo"
            }
            emit(NetworkResult.Error(errorMessage, response.code()))
        }
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Error desconocido"))
    }

    // ==================== PERIOD DAYS ====================

    /**
     * Agrega un día de periodo
     */
    fun addPeriodDay(periodDate: String, isPeriodDay: Boolean = true): Flow<NetworkResult<PeriodDayResponse>> = flow {
        emit(NetworkResult.Loading)

        val request = PeriodDayRequest(periodDate, isPeriodDay)
        val response = cycleApi.addPeriodDay(request)

        if (response.isSuccessful && response.body() != null) {
            emit(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                gson.fromJson(errorBody, ErrorResponse::class.java).detail
            } catch (e: Exception) {
                "Error al marcar día de periodo"
            }
            emit(NetworkResult.Error(errorMessage, response.code()))
        }
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Error desconocido"))
    }

    /**
     * Agrega múltiples días de periodo
     */
    fun addPeriodDaysBatch(dates: List<String>, isPeriodDay: Boolean = true): Flow<NetworkResult<List<PeriodDayResponse>>> = flow {
        emit(NetworkResult.Loading)

        val request = PeriodDayBatchRequest(dates, isPeriodDay)
        val response = cycleApi.addPeriodDaysBatch(request)

        if (response.isSuccessful && response.body() != null) {
            emit(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                gson.fromJson(errorBody, ErrorResponse::class.java).detail
            } catch (e: Exception) {
                "Error al marcar días de periodo"
            }
            emit(NetworkResult.Error(errorMessage, response.code()))
        }
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Error desconocido"))
    }

    /**
     * Obtiene días de periodo en un rango
     */
    fun getPeriodDays(startDate: String, endDate: String): Flow<NetworkResult<List<PeriodDayResponse>>> = flow {
        emit(NetworkResult.Loading)

        val response = cycleApi.getPeriodDays(startDate, endDate)

        if (response.isSuccessful && response.body() != null) {
            emit(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                gson.fromJson(errorBody, ErrorResponse::class.java).detail
            } catch (e: Exception) {
                "Error al obtener días de periodo"
            }
            emit(NetworkResult.Error(errorMessage, response.code()))
        }
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Error desconocido"))
    }

    // ==================== SYMPTOMS ====================

    /**
     * Agrega síntomas diarios
     */
    fun addDailySymptoms(request: DailySymptomsRequest): Flow<NetworkResult<DailySymptomsResponse>> = flow {
        emit(NetworkResult.Loading)

        val response = cycleApi.addDailySymptoms(request)

        if (response.isSuccessful && response.body() != null) {
            emit(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                gson.fromJson(errorBody, ErrorResponse::class.java).detail
            } catch (e: Exception) {
                "Error al registrar síntomas"
            }
            emit(NetworkResult.Error(errorMessage, response.code()))
        }
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Error desconocido"))
    }

    /**
     * Obtiene síntomas de un día
     */
    fun getDailySymptoms(symptomDate: String): Flow<NetworkResult<DailySymptomsResponse>> = flow {
        emit(NetworkResult.Loading)

        val response = cycleApi.getDailySymptoms(symptomDate)

        if (response.isSuccessful && response.body() != null) {
            emit(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                gson.fromJson(errorBody, ErrorResponse::class.java).detail
            } catch (e: Exception) {
                "Error al obtener síntomas"
            }
            emit(NetworkResult.Error(errorMessage, response.code()))
        }
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Error desconocido"))
    }

    /**
     * Actualiza síntomas de un día
     */
    fun updateDailySymptoms(symptomDate: String, request: DailySymptomsRequest): Flow<NetworkResult<DailySymptomsResponse>> = flow {
        emit(NetworkResult.Loading)

        val response = cycleApi.updateDailySymptoms(symptomDate, request)

        if (response.isSuccessful && response.body() != null) {
            emit(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                gson.fromJson(errorBody, ErrorResponse::class.java).detail
            } catch (e: Exception) {
                "Error al actualizar síntomas"
            }
            emit(NetworkResult.Error(errorMessage, response.code()))
        }
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Error desconocido"))
    }

    // ==================== OVULATION ====================

    /**
     * Obtiene información de ovulación
     */
    fun getOvulationInfo(): Flow<NetworkResult<OvulationInfoResponse>> = flow {
        emit(NetworkResult.Loading)

        val response = cycleApi.getOvulationInfo()

        if (response.isSuccessful && response.body() != null) {
            emit(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                gson.fromJson(errorBody, ErrorResponse::class.java).detail
            } catch (e: Exception) {
                "Error al obtener información de ovulación"
            }
            emit(NetworkResult.Error(errorMessage, response.code()))
        }
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Error desconocido"))
    }
}