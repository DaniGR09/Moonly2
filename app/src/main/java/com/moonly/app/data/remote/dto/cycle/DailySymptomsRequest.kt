package com.moonly.app.data.remote.dto.cycle

import com.google.gson.annotations.SerializedName

data class DailySymptomsRequest(
    @SerializedName("symptom_date")
    val symptomDate: String, // "yyyy-MM-dd"

    @SerializedName("bleeding_amount")
    val bleedingAmount: String? = null, // "ligero", "moderado", "abundante", "muy_abundante"

    @SerializedName("bleeding_color")
    val bleedingColor: String? = null, // "rojo_brillante", "rojo_oscuro", "marron", "negro"

    @SerializedName("pain_level")
    val painLevel: Int? = null, // 0-10

    @SerializedName("cravings")
    val cravings: String? = null, // texto libre

    @SerializedName("flow_color")
    val flowColor: String? = null, // "transparente", "blanco", "amarillo", "marron"

    @SerializedName("emotions")
    val emotions: String? = null // JSON string: ["feliz", "cansada"]
)