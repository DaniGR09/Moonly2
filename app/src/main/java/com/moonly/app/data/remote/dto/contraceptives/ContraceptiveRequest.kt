package com.moonly.app.data.remote.dto.contraceptives

import com.google.gson.annotations.SerializedName

data class ContraceptiveCreateRequest(
    @SerializedName("contraceptive_type")
    val contraceptiveType: String, // "pastilla", "inyeccion", "parche", "anillo", "implante", "diu", "preservativo", "otro"

    @SerializedName("contraceptive_name")
    val contraceptiveName: String? = null,

    @SerializedName("notification_time")
    val notificationTime: String? = null, // "HH:mm:ss"

    @SerializedName("notification_enabled")
    val notificationEnabled: Boolean = false,

    @SerializedName("notes")
    val notes: String? = null,

    @SerializedName("start_date")
    val startDate: String? = null,

    @SerializedName("end_date")
    val endDate: String? = null
)

data class ContraceptiveUpdateRequest(
    @SerializedName("contraceptive_type")
    val contraceptiveType: String? = null,

    @SerializedName("contraceptive_name")
    val contraceptiveName: String? = null,

    @SerializedName("notification_time")
    val notificationTime: String? = null,

    @SerializedName("notification_enabled")
    val notificationEnabled: Boolean? = null,

    @SerializedName("notes")
    val notes: String? = null,

    @SerializedName("start_date")
    val startDate: String? = null,

    @SerializedName("end_date")
    val endDate: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean? = null
)