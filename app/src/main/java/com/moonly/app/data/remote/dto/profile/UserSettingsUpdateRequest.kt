package com.moonly.app.data.remote.dto.profile

import com.google.gson.annotations.SerializedName

data class UserSettingsUpdateRequest(
    @SerializedName("motivational_messages_enabled")
    val motivationalMessagesEnabled: Boolean? = null,

    @SerializedName("breast_exam_reminder_enabled")
    val breastExamReminderEnabled: Boolean? = null,

    @SerializedName("average_cycle_length")
    val averageCycleLength: Int? = null,

    @SerializedName("average_period_length")
    val averagePeriodLength: Int? = null,

    @SerializedName("fcm_token")
    val fcmToken: String? = null
)