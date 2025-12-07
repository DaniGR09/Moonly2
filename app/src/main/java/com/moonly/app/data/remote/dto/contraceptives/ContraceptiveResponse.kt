package com.moonly.app.data.remote.dto.contraceptives

import com.google.gson.annotations.SerializedName

data class ContraceptiveResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("contraceptive_type")
    val contraceptiveType: String,

    @SerializedName("contraceptive_name")
    val contraceptiveName: String? = null,

    @SerializedName("notification_time")
    val notificationTime: String? = null,

    @SerializedName("notification_enabled")
    val notificationEnabled: Boolean,

    @SerializedName("notes")
    val notes: String? = null,

    @SerializedName("start_date")
    val startDate: String? = null,

    @SerializedName("end_date")
    val endDate: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)