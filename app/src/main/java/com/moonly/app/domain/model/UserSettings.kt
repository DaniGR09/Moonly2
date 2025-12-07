package com.moonly.app.domain.model

import com.moonly.app.data.remote.dto.profile.UserSettingsResponse

/**
 * Modelo de dominio para Configuraciones de Usuario
 */
data class UserSettings(
    val userId: String,
    val motivationalMessagesEnabled: Boolean,
    val breastExamReminderEnabled: Boolean,
    val lastBreastExamDate: String?,
    val nextBreastExamReminder: String?,
    val averageCycleLength: Int,
    val averagePeriodLength: Int,
    val fcmToken: String?,
    val createdAt: String,
    val updatedAt: String
)

/**
 * Mapper: DTO -> Domain
 */
fun UserSettingsResponse.toUserSettings(): UserSettings {
    return UserSettings(
        userId = this.userId,
        motivationalMessagesEnabled = this.motivationalMessagesEnabled,
        breastExamReminderEnabled = this.breastExamReminderEnabled,
        lastBreastExamDate = this.lastBreastExamDate,
        nextBreastExamReminder = this.nextBreastExamReminder,
        averageCycleLength = this.averageCycleLength,
        averagePeriodLength = this.averagePeriodLength,
        fcmToken = this.fcmToken,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}