package com.moonly.app.data.remote.dto.care_methods

import com.google.gson.annotations.SerializedName

data class CareMethodCreateRequest(
    @SerializedName("method_type")
    val methodType: String, // "copa_menstrual", "toalla_sanitaria", "tampon", "toalla_reutilizable", "ropa_interior_menstrual", "otro"

    @SerializedName("reminder_enabled")
    val reminderEnabled: Boolean = false,

    @SerializedName("change_interval_hours")
    val changeIntervalHours: Int = 4 // 1-24
)

data class CareMethodUpdateRequest(
    @SerializedName("method_type")
    val methodType: String? = null,

    @SerializedName("reminder_enabled")
    val reminderEnabled: Boolean? = null,

    @SerializedName("change_interval_hours")
    val changeIntervalHours: Int? = null,

    @SerializedName("is_active")
    val isActive: Boolean? = null
)

data class CareMethodChangeRequest(
    @SerializedName("method_id")
    val methodId: String
)