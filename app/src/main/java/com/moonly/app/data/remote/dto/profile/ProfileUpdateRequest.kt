package com.moonly.app.data.remote.dto.profile

import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    @SerializedName("nickname")
    val nickname: String? = null,

    @SerializedName("birth_year")
    val birthYear: Int? = null
)