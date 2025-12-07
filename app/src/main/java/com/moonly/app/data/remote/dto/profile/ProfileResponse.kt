package com.moonly.app.data.remote.dto.profile

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("nickname")
    val nickname: String? = null,

    @SerializedName("birth_year")
    val birthYear: Int? = null,

    @SerializedName("email_verified_at")
    val emailVerifiedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("contraceptives")
    val contraceptives: List<ContraceptiveDto>? = null,

    @SerializedName("care_methods")
    val careMethods: List<CareMethodDto>? = null,

    @SerializedName("medical_conditions")
    val medicalConditions: List<MedicalConditionDto>? = null
)

// DTOs simplificados para las relaciones
data class ContraceptiveDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("contraceptive_type")
    val contraceptiveType: String,

    @SerializedName("contraceptive_name")
    val contraceptiveName: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean
)

data class CareMethodDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("method_type")
    val methodType: String,

    @SerializedName("is_active")
    val isActive: Boolean
)

data class MedicalConditionDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("condition_type")
    val conditionType: String,

    @SerializedName("condition_name")
    val conditionName: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean
)