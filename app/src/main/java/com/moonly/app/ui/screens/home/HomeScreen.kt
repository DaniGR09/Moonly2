package com.moonly.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moonly.app.ui.components.calendar.MonthCalendar
import com.moonly.app.ui.components.home.RegistryBottomSheet
import com.moonly.app.ui.theme.*

/**
 * Pantalla principal de Home con:
 * - Calendario mensual
 * - Panel de registro deslizable (Bottom Sheet)
 * - Botón para editar período
 */
@Composable
fun HomeScreen(
    onNavigateToEditPeriod: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Mostrar mensaje de error si existe
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            // TODO: Mostrar Snackbar o Toast
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header con logo y menú
            HomeHeader(
                onMenuClick = onNavigateToProfile
            )

            // Calendario mensual
            MonthCalendar(
                yearMonth = uiState.currentMonth,
                selectedDate = uiState.selectedDate,
                periodDays = uiState.periodDays,
                ovulationDate = uiState.ovulationDate,
                fertileWindowStart = uiState.fertileWindowStart,
                fertileWindowEnd = uiState.fertileWindowEnd,
                daysWithSymptoms = uiState.daysWithSymptoms,
                onDateSelected = { date -> viewModel.selectDate(date) },
                onPreviousMonth = { viewModel.navigateToPreviousMonth() },
                onNextMonth = { viewModel.navigateToNextMonth() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para editar período (más pequeño)
            EditPeriodButton(
                onClick = onNavigateToEditPeriod,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // Panel de registro (Bottom Sheet)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            RegistryBottomSheet(
                selectedDate = uiState.selectedDate,
                currentSymptoms = uiState.currentSymptoms,
                bottomSheetState = uiState.bottomSheetState,
                onToggleSheet = { viewModel.toggleBottomSheet() },
                onClose = { viewModel.clearDateSelection() },
                onEmotionSelected = { emotion -> viewModel.updateEmotion(emotion) },
                onBleedingAmountSelected = { amount -> viewModel.updateBleedingAmount(amount) },
                onBleedingColorSelected = { color -> viewModel.updateBleedingColor(color) },
                onFlowColorSelected = { color -> viewModel.updateFlowColor(color) },
                onCravingsChanged = { cravings -> viewModel.updateCravings(cravings) },
                onPainLevelSelected = { level -> viewModel.updatePainLevel(level) }
            )
        }
    }
}

/**
 * Header de la pantalla con logo y menú
 */
@Composable
private fun HomeHeader(
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MoonlyPurpleMedium),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🌙",
                    fontSize = 18.sp
                )
            }

            Text(
                text = "Moonly",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MoonlyPurpleDark
            )
        }

        // Botón de Perfil (ícono de engranaje)
        IconButton(onClick = onMenuClick) {
            Text(
                text = "⚙️",
                fontSize = 22.sp
            )
        }
    }
}

/**
 * Botón para abrir la pantalla de edición de período (más compacto)
 */
@Composable
private fun EditPeriodButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📅",
                fontSize = 18.sp
            )
            Text(
                text = "Editar período",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MoonlyPurpleMedium
            )
        }
    }
}