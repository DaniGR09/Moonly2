package com.moonly.app.data.remote.dto.cycle

import com.google.gson.annotations.SerializedName

data class DailySymptomsResponse(
    @SerializedName("id")
    val id: String?,  // ← Cambiado a nullable

    @SerializedName("user_id")
    val userId: String?,  // ← Cambiado a nullable

    @SerializedName("symptom_date")
    val symptomDate: String?,  // ← Cambiado a nullable

    @SerializedName("bleeding_amount")
    val bleedingAmount: String? = null,

    @SerializedName("bleeding_color")
    val bleedingColor: String? = null,

    @SerializedName("pain_level")
    val painLevel: Int? = null,

    @SerializedName("cravings")
    val cravings: String? = null,

    @SerializedName("flow_color")
    val flowColor: String? = null,

    @SerializedName("emotions")
    val emotions: String? = null,

    @SerializedName("created_at")
    val createdAt: String?,  // ← Cambiado a nullable

    @SerializedName("updated_at")
    val updatedAt: String?  // ← Cambiado a nullable
)