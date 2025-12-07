package com.moonly.app.domain.usecase.cycle

import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.dto.cycle.DailySymptomsRequest
import com.moonly.app.data.repository.CycleRepository
import com.moonly.app.domain.model.DailySymptoms
import com.moonly.app.domain.model.toDailySymptoms
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Caso de uso para guardar síntomas diarios
 */
class SaveDailySymptomsUseCase @Inject constructor(
    private val cycleRepository: CycleRepository
) {
    operator fun invoke(request: DailySymptomsRequest): Flow<NetworkResult<DailySymptoms?>> {
        return cycleRepository.addDailySymptoms(request).map { result ->
            when (result) {
                is NetworkResult.Success -> {
                    val symptoms = result.data.toDailySymptoms()
                    if (symptoms != null) {
                        NetworkResult.Success(symptoms)
                    } else {
                        NetworkResult.Error("Error al procesar síntomas", null)
                    }
                }
                is NetworkResult.Error -> NetworkResult.Error(result.message, result.code)
                is NetworkResult.Loading -> NetworkResult.Loading
            }
        }
    }
}