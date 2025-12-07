package com.moonly.app.data.remote.dto.cycle

import com.google.gson.annotations.SerializedName

data class CycleUpdateRequest(
    @SerializedName("cycle_end_date")
    val cycleEndDate: String // "yyyy-MM-dd"
)