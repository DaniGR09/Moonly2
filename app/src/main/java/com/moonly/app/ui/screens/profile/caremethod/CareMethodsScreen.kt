package com.moonly.app.ui.screens.profile.caremethod

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
import com.moonly.app.domain.model.CareMethodType
import com.moonly.app.ui.theme.*

/**
 * Pantalla de Método de Cuidado Menstrual
 */
@Composable
fun CareMethodsScreen(
    onNavigateBack: () -> Unit,
    viewModel: CareMethodsViewModel = hiltViewModel()
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
                        text = "Método de cuidado",
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
                    text = "Selecciona tu método de cuidado actual:",
                    fontSize = 14.sp,
                    color = Gray600,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(CareMethodType.values().toList()) { type ->
                MethodOption(
                    type = type,
                    isSelected = uiState.selectedMethod == type,
                    onSelect = { viewModel.selectMethod(type) }
                )
            }
        }
    }
}

/**
 * Opción de método de cuidado
 */
@Composable
private fun MethodOption(
    type: CareMethodType,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MoonlyPurpleMedium else White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onSelect
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = type.getDisplayName(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) White else MoonlyPurpleDark
                )

                Text(
                    text = "Cambio recomendado: cada ${type.getRecommendedIntervalHours()}h",
                    fontSize = 12.sp,
                    color = if (isSelected) White.copy(alpha = 0.8f) else Gray500
                )
            }

            if (isSelected) {
                Text(
                    text = "✓",
                    fontSize = 20.sp,
                    color = White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}