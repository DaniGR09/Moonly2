package com.moonly.app.ui.screens.onboarding.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moonly.app.domain.model.MedicalConditionType

@Composable
fun MedicalConditionsStep(
    selectedConditions: Set<MedicalConditionType>,
    onConditionsChange: (Set<MedicalConditionType>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Título
        Text(
            text = "¿Tienes alguna de estas afecciones?",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de opciones (multi-selección)
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                MedicalConditionOption(
                    text = "Ninguna de las anteriores",
                    selected = selectedConditions.isEmpty(),
                    onClick = { onConditionsChange(emptySet()) }
                )
            }

            items(MedicalConditionType.values().filter { it != MedicalConditionType.OTRA }) { type ->
                MedicalConditionOption(
                    text = type.getDisplayName(),
                    selected = selectedConditions.contains(type),
                    onClick = {
                        val newSet = if (selectedConditions.contains(type)) {
                            selectedConditions - type
                        } else {
                            selectedConditions + type
                        }
                        onConditionsChange(newSet)
                    }
                )
            }
        }
    }
}

@Composable
private fun MedicalConditionOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = { onClick() }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}