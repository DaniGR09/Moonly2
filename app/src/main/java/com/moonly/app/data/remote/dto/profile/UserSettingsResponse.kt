package com.moonly.app.data.remote.dto.profile

import com.google.gson.annotations.SerializedName

data class UserSettingsResponse(
    @SerializedName("user_id")
    val userId: String,

    @SerializedName("motivational_messages_enabled")
    val motivationalMessagesEnabled: Boolean,

    @SerializedName("breast_exam_reminder_enabled")
    val breastExamReminderEnabled: Boolean,

    @SerializedName("last_breast_exam_date")
    val lastBreastExamDate: String? = null,

    @SerializedName("next_breast_exam_reminder")
    val nextBreastExamReminder: String? = null,

    @SerializedName("average_cycle_length")
    val averageCycleLength: Int,

    @SerializedName("average_period_length")
    val averagePeriodLength: Int,

    @SerializedName("fcm_token")
    val fcmToken: String? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)