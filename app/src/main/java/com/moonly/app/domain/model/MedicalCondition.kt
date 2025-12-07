package com.moonly.app.domain.model

import com.moonly.app.data.remote.dto.medical.MedicalConditionResponse
import java.time.LocalDate

/**
 * Modelo de dominio para Condición Médica
 */
data class MedicalCondition(
    val id: String,
    val userId: String,
    val conditionType: MedicalConditionType,
    val conditionName: String?,
    val diagnosedDate: LocalDate?,
    val notes: String?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)

/**
 * Enum para tipos de condiciones médicas
 */
enum class MedicalConditionType {
    OVARIO_POLIQUISTICO,
    OVARIO_MULTIFOLICULAR,
    ENDOMETRIOSIS,
    MIOMAS,
    AMENORREA,
    DISMENORREA,
    OTRA;

    fun toApiValue(): String = name.lowercase()

    fun getDisplayName(): String = when (this) {
        OVARIO_POLIQUISTICO -> "Ovario poliquístico (SOP)"
        OVARIO_MULTIFOLICULAR -> "Ovario multifolicular"
        ENDOMETRIOSIS -> "Endometriosis"
        MIOMAS -> "Miomas"
        AMENORREA -> "Amenorrea"
        DISMENORREA -> "Dismenorrea"
        OTRA -> "Otra"
    }

    companion object {
        fun fromApiValue(value: String): MedicalConditionType? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

/**
 * Mapper: DTO -> Domain
 */
fun MedicalConditionResponse.toMedicalCondition(): MedicalCondition {
    return MedicalCondition(
        id = this.id,
        userId = this.userId,
        conditionType = MedicalConditionType.fromApiValue(this.conditionType) ?: MedicalConditionType.OTRA,
        conditionName = this.conditionName,
        diagnosedDate = this.diagnosedDate?.let { LocalDate.parse(it) },
        notes = this.notes,
        isActive = this.isActive,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}