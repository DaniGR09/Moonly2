package com.moonly.app.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)