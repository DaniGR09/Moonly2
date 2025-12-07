package com.moonly.app.ui.screens.profile.medical

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moonly.app.domain.model.MedicalConditionType
import com.moonly.app.ui.theme.*

/**
 * Pantalla de Condiciones Médicas
 */
@Composable
fun MedicalConditionsScreen(
    onNavigateBack: () -> Unit,
    viewModel: MedicalConditionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Text(text = "←", fontSize = 24.sp, color = MoonlyPurpleMedium)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Condiciones médicas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoonlyPurpleDark
                    )
                }
            }
        },
        containerColor = Gray50
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Selecciona las condiciones que te apliquen:",
                    fontSize = 14.sp,
                    color = Gray600,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(MedicalConditionType.values().toList()) { type ->
                ConditionCheckbox(
                    type = type,
                    isChecked = uiState.selectedConditions.contains(type),
                    onCheckedChange = { checked ->
                        viewModel.toggleCondition(type, checked)
                    }
                )
            }
        }
    }
}

/**
 * Checkbox de condición médica
 */
@Composable
private fun ConditionCheckbox(
    type: MedicalConditionType,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) MoonlyPinkLight else White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = type.getDisplayName(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MoonlyPurpleDark,
                modifier = Modifier.weight(1f)
            )

            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MoonlyPurpleMedium,
                    uncheckedColor = Gray400
                )
            )
        }
    }
}