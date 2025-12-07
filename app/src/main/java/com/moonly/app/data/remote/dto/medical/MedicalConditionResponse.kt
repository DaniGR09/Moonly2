package com.moonly.app.data.remote.dto.medical

import com.google.gson.annotations.SerializedName

data class MedicalConditionResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("condition_type")
    val conditionType: String,

    @SerializedName("condition_name")
    val conditionName: String? = null,

    @SerializedName("diagnosed_date")
    val diagnosedDate: String? = null,

    @SerializedName("notes")
    val notes: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)