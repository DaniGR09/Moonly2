package com.moonly.app.domain.model

import com.moonly.app.data.remote.dto.notifications.NotificationResponse

/**
 * Modelo de dominio para Notificación
 */
data class Notification(
    val id: String,
    val userId: String,
    val notificationType: NotificationType,
    val title: String,
    val message: String,
    val isSent: Boolean,
    val sentAt: String?,
    val createdAt: String
)

/**
 * Enum para tipos de notificaciones
 */
enum class NotificationType {
    ANTICONCEPTIVO,
    METODO_CUIDADO,
    MOTIVACIONAL,
    OVULACION,
    PERIODO_PROXIMO;

    fun toApiValue(): String = name.lowercase()

    fun getDisplayName(): String = when (this) {
        ANTICONCEPTIVO -> "Anticonceptivo"
        METODO_CUIDADO -> "Método de cuidado"
        MOTIVACIONAL -> "Mensaje motivacional"
        OVULACION -> "Ovulación"
        PERIODO_PROXIMO -> "Periodo próximo"
    }

    companion object {
        fun fromApiValue(value: String): NotificationType? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

/**
 * Mapper: DTO -> Domain
 */
fun NotificationResponse.toNotification(): Notification {
    return Notification(
        id = this.id,
        userId = this.userId,
        notificationType = NotificationType.fromApiValue(this.notificationType) ?: NotificationType.MOTIVACIONAL,
        title = this.title,
        message = this.message,
        isSent = this.isSent,
        sentAt = this.sentAt,
        createdAt = this.createdAt
    )
}