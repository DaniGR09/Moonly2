package com.moonly.app.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class PasswordResetRequest(
    @SerializedName("email")
    val email: String
)