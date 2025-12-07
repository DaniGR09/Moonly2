package com.moonly.app.data.remote.dto.cycle

import com.google.gson.annotations.SerializedName

data class PeriodDayRequest(
    @SerializedName("period_date")
    val periodDate: String, // "yyyy-MM-dd"

    @SerializedName("is_period_day")
    val isPeriodDay: Boolean = true
)

data class PeriodDayBatchRequest(
    @SerializedName("dates")
    val dates: List<String>, // ["yyyy-MM-dd", ...]

    @SerializedName("is_period_day")
    val isPeriodDay: Boolean = true
)