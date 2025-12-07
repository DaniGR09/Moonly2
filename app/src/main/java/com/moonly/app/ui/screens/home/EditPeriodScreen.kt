package com.moonly.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moonly.app.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

/**
 * Pantalla para editar período (selección múltiple de días)
 */
@Composable
fun EditPeriodScreen(
    onNavigateBack: () -> Unit,
    viewModel: EditPeriodViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Mostrar mensaje de éxito y volver atrás
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            EditPeriodTopBar(
                currentMonth = uiState.currentMonth,
                onClose = onNavigateBack,
                onPreviousMonth = { viewModel.navigateToPreviousMonth() },
                onNextMonth = { viewModel.navigateToNextMonth() }
            )
        },
        bottomBar = {
            SaveButton(
                selectedDaysCount = uiState.selectedDays.size,
                onSave = {
                    viewModel.savePeriodDays()
                },
                isLoading = uiState.isSaving
            )
        },
        containerColor = White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Generar meses visibles (3 meses: anterior, actual, siguiente)
                val months = listOf(
                    uiState.currentMonth.minusMonths(1),
                    uiState.currentMonth,
                    uiState.currentMonth.plusMonths(1)
                )

                months.forEach { month ->
                    item {
                        MonthSection(
                            yearMonth = month,
                            selectedDays = uiState.selectedDays,
                            onDayToggle = { date -> viewModel.toggleDay(date) }
                        )
                    }
                }
            }

            // Loading overlay
            if (uiState.isSaving) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MoonlyPurpleMedium)
                }
            }
        }
    }
}

/**
 * TopBar con navegación de mes y botón cerrar
 */
@Composable
private fun EditPeriodTopBar(
    currentMonth: YearMonth,
    onClose: () -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {
        // Fila con botón cerrar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Text(
                    text = "✕",
                    fontSize = 24.sp,
                    color = Gray700
                )
            }

            Text(
                text = "Editar período",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MoonlyPurpleDark
            )

            Spacer(modifier = Modifier.width(48.dp)) // Balance
        }

        // Navegación de mes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousMonth) {
                Text(text = "←", fontSize = 24.sp, color = MoonlyPurpleMedium)
            }

            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentMonth.year}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MoonlyPurpleDark
            )

            IconButton(onClick = onNextMonth) {
                Text(text = "→", fontSize = 24.sp, color = MoonlyPurpleMedium)
            }
        }

        Divider(color = Gray200, thickness = 1.dp)
    }
}

/**
 * Sección de un mes completo
 */
@Composable
private fun MonthSection(
    yearMonth: YearMonth,
    selectedDays: Set<LocalDate>,
    onDayToggle: (LocalDate) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Título del mes
        Text(
            text = yearMonth.month.getDisplayName(TextStyle.FULL, Locale("es")),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Gray700
        )

        // Grid de días
        CalendarGrid(
            yearMonth = yearMonth,
            selectedDays = selectedDays,
            onDayToggle = onDayToggle
        )
    }
}

/**
 * Grid del calendario
 */
@Composable
private fun CalendarGrid(
    yearMonth: YearMonth,
    selectedDays: Set<LocalDate>,
    onDayToggle: (LocalDate) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Header de días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("L", "M", "M", "J", "V", "S", "D").forEach { day ->
                Text(
                    text = day,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray500,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Días del mes
        val days = generateDaysForMonth(yearMonth)
        days.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { date ->
                    if (date != null) {
                        DayCell(
                            date = date,
                            isSelected = selectedDays.contains(date),
                            isCurrentMonth = date.year == yearMonth.year && date.month == yearMonth.month,
                            onToggle = { onDayToggle(date) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

/**
 * Celda de un día
 */
@Composable
private fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> Color(0xFFE57373) // Rojo período
                    else -> Color.Transparent
                }
            )
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = if (isCurrentMonth) Gray300 else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 14.sp,
            color = when {
                isSelected -> White
                isCurrentMonth -> MoonlyPurpleDark
                else -> Gray400
            },
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/**
 * Botón de guardar
 */
@Composable
private fun SaveButton(
    selectedDaysCount: Int,
    onSave: () -> Unit,
    isLoading: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Contador
            if (selectedDaysCount > 0) {
                Text(
                    text = "$selectedDaysCount ${if (selectedDaysCount == 1) "día seleccionado" else "días seleccionados"}",
                    fontSize = 13.sp,
                    color = Gray600,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Botón
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE57373),
                    contentColor = White,
                    disabledContainerColor = Gray300,
                    disabledContentColor = Gray500
                ),
                shape = RoundedCornerShape(26.dp),
                enabled = !isLoading && selectedDaysCount > 0
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Guardar cambios",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Genera los días del mes para el grid
 */
private fun generateDaysForMonth(yearMonth: YearMonth): List<LocalDate?> {
    val firstDay = yearMonth.atDay(1)
    val lastDay = yearMonth.atEndOfMonth()

    // Días vacíos al inicio
    val firstDayOfWeek = firstDay.dayOfWeek.value // 1 = Lunes
    val emptyDaysStart = firstDayOfWeek - 1

    val days = mutableListOf<LocalDate?>()

    // Agregar días vacíos
    repeat(emptyDaysStart) {
        days.add(null)
    }

    // Agregar días del mes
    for (day in 1..lastDay.dayOfMonth) {
        days.add(yearMonth.atDay(day))
    }

    // Completar última semana si es necesario
    val remainingDays = 7 - (days.size % 7)
    if (remainingDays < 7) {
        repeat(remainingDays) {
            days.add(null)
        }
    }

    return days
}