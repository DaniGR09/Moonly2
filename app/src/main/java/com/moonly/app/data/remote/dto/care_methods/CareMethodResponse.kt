package com.moonly.app.data.remote.dto.care_methods

import com.google.gson.annotations.SerializedName

data class CareMethodResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("method_type")
    val methodType: String,

    @SerializedName("reminder_enabled")
    val reminderEnabled: Boolean,

    @SerializedName("change_interval_hours")
    val changeIntervalHours: Int,

    @SerializedName("last_change_time")
    val lastChangeTime: String? = null,

    @SerializedName("next_reminder_time")
    val nextReminderTime: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)