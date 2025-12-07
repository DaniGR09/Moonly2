package com.moonly.app.domain.usecase.cycle

import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.repository.CycleRepository
import com.moonly.app.domain.model.PeriodDay
import com.moonly.app.domain.model.toPeriodDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * Caso de uso para agregar día de periodo
 */
class AddPeriodDayUseCase @Inject constructor(
    private val cycleRepository: CycleRepository
) {
    operator fun invoke(periodDate: LocalDate, isPeriodDay: Boolean = true): Flow<NetworkResult<PeriodDay>> {
        return cycleRepository.addPeriodDay(periodDate.toString(), isPeriodDay).map { result ->
            when (result) {
                is NetworkResult.Success -> NetworkResult.Success(result.data.toPeriodDay())
                is NetworkResult.Error -> NetworkResult.Error(result.message, result.code)
                is NetworkResult.Loading -> NetworkResult.Loading
            }
        }
    }
}