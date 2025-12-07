package com.moonly.app.data.remote.dto.medical

import com.google.gson.annotations.SerializedName

data class MedicalConditionCreateRequest(
    @SerializedName("condition_type")
    val conditionType: String, // "ovario_poliquistico", "ovario_multifolicular", "endometriosis", "miomas", "amenorrea", "dismenorrea", "otra"

    @SerializedName("condition_name")
    val conditionName: String? = null, // Para tipo "otra"

    @SerializedName("diagnosed_date")
    val diagnosedDate: String? = null,

    @SerializedName("notes")
    val notes: String? = null
)

data class MedicalConditionUpdateRequest(
    @SerializedName("condition_type")
    val conditionType: String? = null,

    @SerializedName("condition_name")
    val conditionName: String? = null,

    @SerializedName("diagnosed_date")
    val diagnosedDate: String? = null,

    @SerializedName("notes")
    val notes: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean? = null
)