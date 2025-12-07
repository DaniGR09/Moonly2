package com.moonly.app.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("user")
    val user: UserDto,

    @SerializedName("session")
    val session: SessionDto,

    @SerializedName("message")
    val message: String? = null
)

data class UserDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("email_confirmed_at")
    val emailConfirmedAt: String? = null
)

data class SessionDto(
    @SerializedName("access_token")
    val accessToken: String?,

    @SerializedName("refresh_token")
    val refreshToken: String?
)