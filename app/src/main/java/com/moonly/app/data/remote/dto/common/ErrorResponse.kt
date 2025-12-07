package com.moonly.app.data.remote.dto.common

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("detail")
    val detail: String
)