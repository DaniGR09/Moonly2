package com.moonly.app.data.remote.dto.cycle

import com.google.gson.annotations.SerializedName

data class OvulationInfoResponse(
    @SerializedName("estimated_ovulation_date")
    val estimatedOvulationDate: String? = null,

    @SerializedName("fertile_window_start")
    val fertileWindowStart: String? = null,

    @SerializedName("fertile_window_end")
    val fertileWindowEnd: String? = null,

    @SerializedName("is_in_fertile_window")
    val isInFertileWindow: Boolean = false,

    @SerializedName("days_until_ovulation")
    val daysUntilOvulation: Int? = null
)