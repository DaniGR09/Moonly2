package com.moonly.app.data.remote.dto.cycle

import com.google.gson.annotations.SerializedName

data class CycleCreateRequest(
    @SerializedName("cycle_start_date")
    val cycleStartDate: String, // "yyyy-MM-dd"

    @SerializedName("cycle_end_date")
    val cycleEndDate: String? = null
)