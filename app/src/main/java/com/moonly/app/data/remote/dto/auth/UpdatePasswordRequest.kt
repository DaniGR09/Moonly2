package com.moonly.app.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

/**
 * Request para actualizar contraseña con token de recuperación
 */
data class UpdatePasswordRequest(
    @SerializedName("new_password")
    val newPassword: String
)