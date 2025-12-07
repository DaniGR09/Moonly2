package com.moonly.app.data.remote.api

import com.moonly.app.core.constants.ApiConstants
import com.moonly.app.data.remote.dto.auth.*
import com.moonly.app.data.remote.dto.common.MessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST(ApiConstants.Auth.SIGNUP)
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<AuthResponse>

    @POST(ApiConstants.Auth.SIGNIN)
    suspend fun signIn(
        @Body request: SignInRequest
    ): Response<AuthResponse>

    @POST(ApiConstants.Auth.SIGNOUT)
    suspend fun signOut(): Response<MessageResponse>

    @POST(ApiConstants.Auth.RESET_PASSWORD)
    suspend fun resetPassword(
        @Body request: PasswordResetRequest
    ): Response<MessageResponse>

    /**
     * ⚠️ IMPORTANTE: Este endpoint actualiza la contraseña usando el token
     * que viene en el deep link de Supabase
     */
    @POST(ApiConstants.Auth.CHANGE_PASSWORD)
    suspend fun updatePassword(
        @Body request: UpdatePasswordRequest
    ): Response<MessageResponse>

    @POST(ApiConstants.Auth.CHANGE_PASSWORD)
    suspend fun changePassword(
        @Body request: PasswordChangeRequest
    ): Response<MessageResponse>
}