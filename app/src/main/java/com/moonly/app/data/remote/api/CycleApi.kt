package com.moonly.app.data.remote.api

import com.moonly.app.core.constants.ApiConstants
import com.moonly.app.data.remote.dto.cycle.*
import retrofit2.Response
import retrofit2.http.*

interface CycleApi {

    // ==================== CYCLES ====================

    @POST(ApiConstants.Cycle.CREATE_CYCLE)
    suspend fun createCycle(
        @Body request: CycleCreateRequest
    ): Response<CycleResponse>

    @GET(ApiConstants.Cycle.GET_CYCLES)
    suspend fun getCycles(
        @Query("limit") limit: Int = 10
    ): Response<List<CycleResponse>>

    @GET(ApiConstants.Cycle.GET_CURRENT_CYCLE)
    suspend fun getCurrentCycle(): Response<CycleResponse>

    @PUT(ApiConstants.Cycle.UPDATE_CYCLE)
    suspend fun updateCycle(
        @Path("cycle_id") cycleId: String,
        @Body request: CycleUpdateRequest
    ): Response<CycleResponse>

    // ==================== PERIOD DAYS ====================

    @POST(ApiConstants.Cycle.ADD_PERIOD_DAY)
    suspend fun addPeriodDay(
        @Body request: PeriodDayRequest
    ): Response<PeriodDayResponse>

    @POST(ApiConstants.Cycle.ADD_PERIOD_DAYS_BATCH)
    suspend fun addPeriodDaysBatch(
        @Body request: PeriodDayBatchRequest
    ): Response<List<PeriodDayResponse>>

    @GET(ApiConstants.Cycle.GET_PERIOD_DAYS)
    suspend fun getPeriodDays(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Response<List<PeriodDayResponse>>

    // ==================== SYMPTOMS ====================

    @POST(ApiConstants.Cycle.ADD_SYMPTOMS)
    suspend fun addDailySymptoms(
        @Body request: DailySymptomsRequest
    ): Response<DailySymptomsResponse>

    @GET(ApiConstants.Cycle.GET_SYMPTOMS)
    suspend fun getDailySymptoms(
        @Path("symptom_date") symptomDate: String
    ): Response<DailySymptomsResponse>

    @PUT(ApiConstants.Cycle.UPDATE_SYMPTOMS)
    suspend fun updateDailySymptoms(
        @Path("symptom_date") symptomDate: String,
        @Body request: DailySymptomsRequest
    ): Response<DailySymptomsResponse>

    // ==================== OVULATION ====================

    @GET(ApiConstants.Cycle.GET_OVULATION_INFO)
    suspend fun getOvulationInfo(): Response<OvulationInfoResponse>
}