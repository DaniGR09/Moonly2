package com.moonly.app.domain.model

import com.moonly.app.data.remote.dto.cycle.PeriodDayResponse
import java.time.LocalDate

/**
 * Modelo de dominio para Día de Periodo
 */
data class PeriodDay(
    val id: String,
    val userId: String,
    val cycleId: String?,
    val periodDate: LocalDate,
    val isPeriodDay: Boolean,
    val createdAt: String,
    val updatedAt: String
)

/**
 * Mapper: DTO -> Domain
 */
fun PeriodDayResponse.toPeriodDay(): PeriodDay {
    return PeriodDay(
        id = this.id,
        userId = this.userId,
        cycleId = this.cycleId,
        periodDate = LocalDate.parse(this.periodDate),
        isPeriodDay = this.isPeriodDay,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}