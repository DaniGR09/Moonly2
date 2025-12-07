package com.moonly.app.data.remote.dto.cycle

import com.google.gson.annotations.SerializedName

data class CycleResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("cycle_start_date")
    val cycleStartDate: String,

    @SerializedName("cycle_end_date")
    val cycleEndDate: String? = null,

    @SerializedName("cycle_length_days")
    val cycleLengthDays: Int? = null,

    @SerializedName("estimated_ovulation_date")
    val estimatedOvulationDate: String? = null,

    @SerializedName("fertile_window_start")
    val fertileWindowStart: String? = null,

    @SerializedName("fertile_window_end")
    val fertileWindowEnd: String? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)