package com.moonly.app.data.remote.api

import com.moonly.app.core.constants.ApiConstants
import com.moonly.app.data.remote.dto.common.MessageResponse
import com.moonly.app.data.remote.dto.contraceptives.*
import retrofit2.Response
import retrofit2.http.*

interface ContraceptivesApi {

    @POST(ApiConstants.Contraceptives.CREATE)
    suspend fun createContraceptive(
        @Body request: ContraceptiveCreateRequest
    ): Response<ContraceptiveResponse>

    @GET(ApiConstants.Contraceptives.GET_ALL)
    suspend fun getContraceptives(
        @Query("active_only") activeOnly: Boolean = true
    ): Response<List<ContraceptiveResponse>>

    @GET(ApiConstants.Contraceptives.GET_BY_ID)
    suspend fun getContraceptive(
        @Path("contraceptive_id") contraceptiveId: String
    ): Response<ContraceptiveResponse>

    @PUT(ApiConstants.Contraceptives.UPDATE)
    suspend fun updateContraceptive(
        @Path("contraceptive_id") contraceptiveId: String,
        @Body request: ContraceptiveUpdateRequest
    ): Response<ContraceptiveResponse>

    @DELETE(ApiConstants.Contraceptives.DELETE)
    suspend fun deleteContraceptive(
        @Path("contraceptive_id") contraceptiveId: String
    ): Response<MessageResponse>
}