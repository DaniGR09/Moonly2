package com.moonly.app.ui.screens.onboarding.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moonly.app.ui.components.textfields.MoonlyTextField

@Composable
fun BirthYearStep(
    birthYear: Int?,
    onBirthYearChange: (Int?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text(
            text = "¿En qué año naciste?",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        Text(
            text = "Esto nos ayuda a personalizar tu experiencia",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de año
        MoonlyTextField(
            value = birthYear?.toString() ?: "",
            onValueChange = {
                val year = it.toIntOrNull()
                onBirthYearChange(year)
            },
            label = "Año de nacimiento (opcional)",
            placeholder = "Ej: 1995",
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    }
}