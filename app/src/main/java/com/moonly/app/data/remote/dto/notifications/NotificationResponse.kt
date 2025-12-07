package com.moonly.app.data.remote.dto.notifications

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("notification_type")
    val notificationType: String, // "anticonceptivo", "metodo_cuidado", "motivacional", "ovulacion", "periodo_proximo"

    @SerializedName("title")
    val title: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("is_sent")
    val isSent: Boolean,

    @SerializedName("sent_at")
    val sentAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String
)

data class NotificationHistoryResponse(
    @SerializedName("notifications")
    val notifications: List<NotificationResponse>,

    @SerializedName("total")
    val total: Int,

    @SerializedName("page")
    val page: Int,

    @SerializedName("page_size")
    val pageSize: Int
)