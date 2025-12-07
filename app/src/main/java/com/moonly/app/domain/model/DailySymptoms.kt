package com.moonly.app.domain.model

import com.moonly.app.data.remote.dto.cycle.DailySymptomsResponse
import java.time.LocalDate

/**
 * Modelo de dominio para Síntomas Diarios
 */
data class DailySymptoms(
    val id: String,
    val userId: String,
    val symptomDate: LocalDate,
    val bleedingAmount: BleedingAmount?,
    val bleedingColor: BleedingColor?,
    val painLevel: Int?,
    val cravings: String?,
    val flowColor: FlowColor?,
    val emotions: List<Emotion>?,
    val createdAt: String,
    val updatedAt: String
)

/**
 * Enums para síntomas
 */
enum class BleedingAmount {
    LIGERO,
    MODERADO,
    ABUNDANTE,
    MUY_ABUNDANTE;

    fun toApiValue(): String = name.lowercase()

    companion object {
        fun fromApiValue(value: String): BleedingAmount? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

enum class BleedingColor {
    ROJO_BRILLANTE,
    ROJO_OSCURO,
    MARRON,
    NEGRO;

    fun toApiValue(): String = name.lowercase()

    companion object {
        fun fromApiValue(value: String): BleedingColor? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

enum class FlowColor {
    TRANSPARENTE,
    BLANCO,
    AMARILLO,
    MARRON;

    fun toApiValue(): String = name.lowercase()

    companion object {
        fun fromApiValue(value: String): FlowColor? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

enum class Emotion {
    FELIZ,
    TRISTE,
    ESTRESADO,
    ANSIOSO,
    ABURRIDO,
    EMOCIONADO,
    MOTIVADO,
    CANSADO,
    RELAJADO,
    NOSTALGICO,
    FRUSTRADO,
    APATICO;

    fun toApiValue(): String = name.lowercase()

    companion object {
        fun fromApiValue(value: String): Emotion? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }

        fun fromJsonArray(json: String?): List<Emotion>? {
            if (json.isNullOrBlank()) return null
            return try {
                json.trim('[', ']')
                    .split(",")
                    .map { it.trim().trim('"') }
                    .mapNotNull { fromApiValue(it) }
            } catch (e: Exception) {
                null
            }
        }

        fun toJsonArray(emotions: List<Emotion>?): String? {
            if (emotions.isNullOrEmpty()) return null
            return emotions.joinToString(prefix = "[", postfix = "]") { "\"${it.toApiValue()}\"" }
        }
    }
}

/**
 * Mapper: DTO -> Domain
 * Retorna null si algún campo requerido es null o si hay error al parsear
 */
fun DailySymptomsResponse.toDailySymptoms(): DailySymptoms? {
    // Valida que los campos requeridos no sean null
    if (this.id == null || this.userId == null || this.symptomDate == null ||
        this.createdAt == null || this.updatedAt == null) {
        return null
    }

    return try {
        DailySymptoms(
            id = this.id,
            userId = this.userId,
            symptomDate = LocalDate.parse(this.symptomDate),
            bleedingAmount = this.bleedingAmount?.let { BleedingAmount.fromApiValue(it) },
            bleedingColor = this.bleedingColor?.let { BleedingColor.fromApiValue(it) },
            painLevel = this.painLevel,
            cravings = this.cravings,
            flowColor = this.flowColor?.let { FlowColor.fromApiValue(it) },
            emotions = Emotion.fromJsonArray(this.emotions),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    } catch (e: Exception) {
        // Si hay algún error al parsear (por ejemplo, fecha inválida), retorna null
        null
    }
}