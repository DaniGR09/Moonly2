package com.moonly.app.domain.model

import com.moonly.app.data.remote.dto.contraceptives.ContraceptiveResponse
import java.time.LocalDate
import java.time.LocalTime

/**
 * Modelo de dominio para Anticonceptivo
 */
data class Contraceptive(
    val id: String,
    val userId: String,
    val contraceptiveType: ContraceptiveType,
    val contraceptiveName: String?,
    val notificationTime: LocalTime?,
    val notificationEnabled: Boolean,
    val notes: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)

/**
 * Enum para tipos de anticonceptivos
 */
enum class ContraceptiveType {
    PASTILLA,
    INYECCION,
    PARCHE,
    ANILLO,
    IMPLANTE,
    DIU,
    PRESERVATIVO,
    OTRO;

    fun toApiValue(): String = name.lowercase()

    fun getDisplayName(): String = when (this) {
        PASTILLA -> "Pastilla"
        INYECCION -> "Inyección"
        PARCHE -> "Parche"
        ANILLO -> "Anillo"
        IMPLANTE -> "Implante"
        DIU -> "DIU"
        PRESERVATIVO -> "Preservativo"
        OTRO -> "Otro"
    }

    companion object {
        fun fromApiValue(value: String): ContraceptiveType? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

/**
 * Mapper: DTO -> Domain
 */
fun ContraceptiveResponse.toContraceptive(): Contraceptive {
    return Contraceptive(
        id = this.id,
        userId = this.userId,
        contraceptiveType = ContraceptiveType.fromApiValue(this.contraceptiveType) ?: ContraceptiveType.OTRO,
        contraceptiveName = this.contraceptiveName,
        notificationTime = this.notificationTime?.let { LocalTime.parse(it) },
        notificationEnabled = this.notificationEnabled,
        notes = this.notes,
        startDate = this.startDate?.let { LocalDate.parse(it) },
        endDate = this.endDate?.let { LocalDate.parse(it) },
        isActive = this.isActive,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}