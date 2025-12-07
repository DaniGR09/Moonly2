package com.moonly.app.data.remote.api

import com.moonly.app.core.constants.ApiConstants
import com.moonly.app.data.remote.dto.care_methods.*
import com.moonly.app.data.remote.dto.common.MessageResponse
import retrofit2.Response
import retrofit2.http.*

interface CareMethodsApi {

    @POST(ApiConstants.CareMethods.CREATE)
    suspend fun createCareMethod(
        @Body request: CareMethodCreateRequest
    ): Response<CareMethodResponse>

    @GET(ApiConstants.CareMethods.GET_ALL)
    suspend fun getCareMethods(
        @Query("active_only") activeOnly: Boolean = true
    ): Response<List<CareMethodResponse>>

    @GET(ApiConstants.CareMethods.GET_BY_ID)
    suspend fun getCareMethod(
        @Path("method_id") methodId: String
    ): Response<CareMethodResponse>

    @PUT(ApiConstants.CareMethods.UPDATE)
    suspend fun updateCareMethod(
        @Path("method_id") methodId: String,
        @Body request: CareMethodUpdateRequest
    ): Response<CareMethodResponse>

    @POST(ApiConstants.CareMethods.REGISTER_CHANGE)
    suspend fun registerChange(
        @Body request: CareMethodChangeRequest
    ): Response<CareMethodResponse>

    @DELETE(ApiConstants.CareMethods.DELETE)
    suspend fun deleteCareMethod(
        @Path("method_id") methodId: String
    ): Response<MessageResponse>
}