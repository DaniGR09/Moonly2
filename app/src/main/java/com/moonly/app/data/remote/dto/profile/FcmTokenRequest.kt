package com.moonly.app.data.remote.dto.profile

import com.google.gson.annotations.SerializedName

data class FcmTokenRequest(
    @SerializedName("fcm_token")
    val fcmToken: String
)