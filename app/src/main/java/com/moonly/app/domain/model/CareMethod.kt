package com.moonly.app.domain.model

import com.moonly.app.data.remote.dto.care_methods.CareMethodResponse
import java.time.LocalDateTime

/**
 * Modelo de dominio para Método de Cuidado
 */
data class CareMethod(
    val id: String,
    val userId: String,
    val methodType: CareMethodType,
    val reminderEnabled: Boolean,
    val changeIntervalHours: Int,
    val lastChangeTime: LocalDateTime?,
    val nextReminderTime: LocalDateTime?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)

/**
 * Enum para tipos de métodos de cuidado
 */
enum class CareMethodType {
    COPA_MENSTRUAL,
    TOALLA_SANITARIA,
    TAMPON,
    TOALLA_REUTILIZABLE,
    ROPA_INTERIOR_MENSTRUAL,
    OTRO;

    fun toApiValue(): String = name.lowercase()

    fun getDisplayName(): String = when (this) {
        COPA_MENSTRUAL -> "Copa menstrual"
        TOALLA_SANITARIA -> "Toalla sanitaria"
        TAMPON -> "Tampón"
        TOALLA_REUTILIZABLE -> "Toalla reutilizable"
        ROPA_INTERIOR_MENSTRUAL -> "Ropa interior menstrual"
        OTRO -> "Otro"
    }

    fun getRecommendedIntervalHours(): Int = when (this) {
        COPA_MENSTRUAL -> 8
        TOALLA_SANITARIA -> 4
        TAMPON -> 4
        TOALLA_REUTILIZABLE -> 4
        ROPA_INTERIOR_MENSTRUAL -> 8
        OTRO -> 4
    }

    companion object {
        fun fromApiValue(value: String): CareMethodType? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

/**
 * Mapper: DTO -> Domain
 */
fun CareMethodResponse.toCareMethod(): CareMethod {
    return CareMethod(
        id = this.id,
        userId = this.userId,
        methodType = CareMethodType.fromApiValue(this.methodType) ?: CareMethodType.OTRO,
        reminderEnabled = this.reminderEnabled,
        changeIntervalHours = this.changeIntervalHours,
        lastChangeTime = this.lastChangeTime?.let { LocalDateTime.parse(it) },
        nextReminderTime = this.nextReminderTime?.let { LocalDateTime.parse(it) },
        isActive = this.isActive,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}