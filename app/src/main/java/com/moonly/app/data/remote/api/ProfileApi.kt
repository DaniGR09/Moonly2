package com.moonly.app.data.remote.api

import com.moonly.app.core.constants.ApiConstants
import com.moonly.app.data.remote.dto.common.MessageResponse
import com.moonly.app.data.remote.dto.profile.*
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @GET(ApiConstants.Profile.GET_PROFILE)
    suspend fun getProfile(): Response<ProfileResponse>

    @PUT(ApiConstants.Profile.UPDATE_PROFILE)
    suspend fun updateProfile(
        @Body request: ProfileUpdateRequest
    ): Response<ProfileResponse>

    @POST(ApiConstants.Profile.FIRST_LOGIN_SETUP)
    suspend fun firstLoginSetup(
        @Body request: FirstLoginSetupRequest
    ): Response<ProfileResponse>

    @DELETE(ApiConstants.Profile.DELETE_ACCOUNT)
    suspend fun deleteAccount(): Response<MessageResponse>

    @GET(ApiConstants.Profile.GET_SETTINGS)
    suspend fun getSettings(): Response<UserSettingsResponse>

    @PUT(ApiConstants.Profile.UPDATE_SETTINGS)
    suspend fun updateSettings(
        @Body request: UserSettingsUpdateRequest
    ): Response<UserSettingsResponse>

    @POST(ApiConstants.Profile.UPDATE_FCM_TOKEN)
    suspend fun updateFcmToken(
        @Body request: FcmTokenRequest
    ): Response<MessageResponse>
}