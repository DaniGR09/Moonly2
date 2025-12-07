package com.moonly.app.domain.model

import com.moonly.app.data.remote.dto.cycle.OvulationInfoResponse
import java.time.LocalDate

/**
 * Modelo de dominio para Información de Ovulación
 */
data class OvulationInfo(
    val estimatedOvulationDate: LocalDate?,
    val fertileWindowStart: LocalDate?,
    val fertileWindowEnd: LocalDate?,
    val isInFertileWindow: Boolean,
    val daysUntilOvulation: Int?
)

/**
 * Mapper: DTO -> Domain
 */
fun OvulationInfoResponse.toOvulationInfo(): OvulationInfo {
    return OvulationInfo(
        estimatedOvulationDate = this.estimatedOvulationDate?.let { LocalDate.parse(it) },
        fertileWindowStart = this.fertileWindowStart?.let { LocalDate.parse(it) },
        fertileWindowEnd = this.fertileWindowEnd?.let { LocalDate.parse(it) },
        isInFertileWindow = this.isInFertileWindow,
        daysUntilOvulation = this.daysUntilOvulation
    )
}