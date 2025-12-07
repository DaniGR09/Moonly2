package com.moonly.app.domain.usecase.cycle

import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.repository.CycleRepository
import com.moonly.app.domain.model.Cycle
import com.moonly.app.domain.model.toCycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Caso de uso para obtener el ciclo actual
 */
class GetCurrentCycleUseCase @Inject constructor(
    private val cycleRepository: CycleRepository
) {
    operator fun invoke(): Flow<NetworkResult<Cycle>> {
        return cycleRepository.getCurrentCycle().map { result ->
            when (result) {
                is NetworkResult.Success -> NetworkResult.Success(result.data.toCycle())
                is NetworkResult.Error -> NetworkResult.Error(result.message, result.code)
                is NetworkResult.Loading -> NetworkResult.Loading
            }
        }
    }
}