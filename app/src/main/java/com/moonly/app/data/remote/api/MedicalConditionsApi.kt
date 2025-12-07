package com.moonly.app.data.remote.api

import com.moonly.app.core.constants.ApiConstants
import com.moonly.app.data.remote.dto.common.MessageResponse
import com.moonly.app.data.remote.dto.medical.*
import retrofit2.Response
import retrofit2.http.*

interface MedicalConditionsApi {

    @POST(ApiConstants.MedicalConditions.CREATE)
    suspend fun createMedicalCondition(
        @Body request: MedicalConditionCreateRequest
    ): Response<MedicalConditionResponse>

    @GET(ApiConstants.MedicalConditions.GET_ALL)
    suspend fun getMedicalConditions(
        @Query("active_only") activeOnly: Boolean = true
    ): Response<List<MedicalConditionResponse>>

    @GET(ApiConstants.MedicalConditions.GET_BY_ID)
    suspend fun getMedicalCondition(
        @Path("condition_id") conditionId: String
    ): Response<MedicalConditionResponse>

    @PUT(ApiConstants.MedicalConditions.UPDATE)
    suspend fun updateMedicalCondition(
        @Path("condition_id") conditionId: String,
        @Body request: MedicalConditionUpdateRequest
    ): Response<MedicalConditionResponse>

    @DELETE(ApiConstants.MedicalConditions.DELETE)
    suspend fun deleteMedicalCondition(
        @Path("condition_id") conditionId: String
    ): Response<MessageResponse>
}