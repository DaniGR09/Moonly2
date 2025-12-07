package com.moonly.app.domain.model

import com.moonly.app.data.remote.dto.cycle.CycleResponse
import java.time.LocalDate

/**
 * Modelo de dominio para Ciclo Menstrual
 */
data class Cycle(
    val id: String,
    val userId: String,
    val cycleStartDate: LocalDate,
    val cycleEndDate: LocalDate?,
    val cycleLengthDays: Int?,
    val estimatedOvulationDate: LocalDate?,
    val fertileWindowStart: LocalDate?,
    val fertileWindowEnd: LocalDate?,
    val createdAt: String,
    val updatedAt: String
)

/**
 * Mapper: DTO -> Domain
 */
fun CycleResponse.toCycle(): Cycle {
    return Cycle(
        id = this.id,
        userId = this.userId,
        cycleStartDate = LocalDate.parse(this.cycleStartDate),
        cycleEndDate = this.cycleEndDate?.let { LocalDate.parse(it) },
        cycleLengthDays = this.cycleLengthDays,
        estimatedOvulationDate = this.estimatedOvulationDate?.let { LocalDate.parse(it) },
        fertileWindowStart = this.fertileWindowStart?.let { LocalDate.parse(it) },
        fertileWindowEnd = this.fertileWindowEnd?.let { LocalDate.parse(it) },
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}