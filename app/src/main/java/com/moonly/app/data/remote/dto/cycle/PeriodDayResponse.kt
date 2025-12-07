package com.moonly.app.data.remote.dto.cycle

import com.google.gson.annotations.SerializedName

data class PeriodDayResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("cycle_id")
    val cycleId: String? = null,

    @SerializedName("period_date")
    val periodDate: String,

    @SerializedName("is_period_day")
    val isPeriodDay: Boolean,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)