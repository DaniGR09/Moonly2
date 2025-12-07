package com.moonly.app.data.remote.api

import com.moonly.app.core.constants.ApiConstants
import com.moonly.app.data.remote.dto.common.MessageResponse
import com.moonly.app.data.remote.dto.notifications.NotificationHistoryResponse
import retrofit2.Response
import retrofit2.http.*

interface NotificationsApi {

    @GET(ApiConstants.Notifications.GET_HISTORY)
    suspend fun getNotificationsHistory(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<NotificationHistoryResponse>

    @POST(ApiConstants.Notifications.PROCESS_PENDING)
    suspend fun processPendingNotifications(): Response<MessageResponse>

    @POST(ApiConstants.Notifications.SEND_TEST)
    suspend fun sendTestNotification(
        @Query("title") title: String,
        @Query("message") message: String
    ): Response<MessageResponse>
}