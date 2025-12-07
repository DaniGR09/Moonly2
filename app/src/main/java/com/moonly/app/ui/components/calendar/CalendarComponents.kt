package com.moonly.app.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moonly.app.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

/**
 * Data class para representar un día del calendario
 */
data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val isPeriodDay: Boolean,
    val isOvulationDay: Boolean,
    val isFertileDay: Boolean,
    val hasSymptoms: Boolean,
    val isFuture: Boolean
)

/**
 * Calendario mensual completo
 */
@Composable
fun MonthCalendar(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    periodDays: Set<LocalDate>,
    ovulationDate: LocalDate?,
    fertileWindowStart: LocalDate?,
    fertileWindowEnd: LocalDate?,
    daysWithSymptoms: Set<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .padding(16.dp)
    ) {
        // Header con navegación de mes
        MonthHeader(
            yearMonth = yearMonth,
            onPreviousMonth = onPreviousMonth,
            onNextMonth = onNextMonth
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Días de la semana (L, M, M, J, V, S, D)
        WeekDaysHeader()

        Spacer(modifier = Modifier.height(8.dp))

        // Grid de días (7 columnas x 5-6 filas)
        CalendarGrid(
            yearMonth = yearMonth,
            selectedDate = selectedDate,
            periodDays = periodDays,
            ovulationDate = ovulationDate,
            fertileWindowStart = fertileWindowStart,
            fertileWindowEnd = fertileWindowEnd,
            daysWithSymptoms = daysWithSymptoms,
            onDateSelected = onDateSelected
        )
    }
}

/**
 * Header del mes con navegación
 */
@Composable
private fun MonthHeader(
    yearMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón anterior
        Text(
            text = "←",
            fontSize = 24.sp,
            color = MoonlyPurpleMedium,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onPreviousMonth() }
                .padding(8.dp)
        )

        // Mes y año
        Text(
            text = "${yearMonth.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${yearMonth.year}",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MoonlyPurpleDark,
            textAlign = TextAlign.Center
        )

        // Botón siguiente
        Text(
            text = "→",
            fontSize = 24.sp,
            color = MoonlyPurpleMedium,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onNextMonth() }
                .padding(8.dp)
        )
    }
}

/**
 * Header con los días de la semana
 */
@Composable
private fun WeekDaysHeader() {
    val weekDays = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        weekDays.forEach { day ->
            Text(
                text = day,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Gray600,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Grid de días del calendario
 */
@Composable
private fun CalendarGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    periodDays: Set<LocalDate>,
    ovulationDate: LocalDate?,
    fertileWindowStart: LocalDate?,
    fertileWindowEnd: LocalDate?,
    daysWithSymptoms: Set<LocalDate>,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = generateCalendarDays(
        yearMonth = yearMonth,
        periodDays = periodDays,
        ovulationDate = ovulationDate,
        fertileWindowStart = fertileWindowStart,
        fertileWindowEnd = fertileWindowEnd,
        daysWithSymptoms = daysWithSymptoms
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        daysInMonth.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { calendarDay ->
                    DayCell(
                        calendarDay = calendarDay,
                        isSelected = calendarDay.date == selectedDate,
                        onDateSelected = onDateSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Celda individual de un día
 */
@Composable
private fun DayCell(
    calendarDay: CalendarDay,
    isSelected: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isSelected -> MoonlyPurpleMedium
        calendarDay.isToday -> MoonlyPinkLight
        calendarDay.isPeriodDay -> Color(0xFFE57373) // Rojo periodo
        calendarDay.isOvulationDay -> MoonlyOvulationTeal
        calendarDay.isFertileDay -> Color(0xFFA5D6A7) // Verde fértil
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> White
        calendarDay.isFuture -> Gray400
        !calendarDay.isCurrentMonth -> Gray400
        else -> MoonlyPurpleDark
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = !calendarDay.isFuture) {
                onDateSelected(calendarDay.date)
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = calendarDay.date.dayOfMonth.toString(),
                fontSize = 14.sp,
                fontWeight = if (calendarDay.isToday) FontWeight.Bold else FontWeight.Normal,
                color = textColor,
                textAlign = TextAlign.Center
            )

            // Indicador de síntomas registrados
            if (calendarDay.hasSymptoms && !isSelected) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(MoonlyPurpleMedium)
                )
            }
        }
    }
}

/**
 * Genera la lista de días para el calendario
 * Incluye días del mes anterior y siguiente para completar semanas
 */
private fun generateCalendarDays(
    yearMonth: YearMonth,
    periodDays: Set<LocalDate>,
    ovulationDate: LocalDate?,
    fertileWindowStart: LocalDate?,
    fertileWindowEnd: LocalDate?,
    daysWithSymptoms: Set<LocalDate>
): List<CalendarDay> {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()
    val today = LocalDate.now()

    // Calcular días del mes anterior que se muestran
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 = Lunes, 7 = Domingo
    val daysFromPreviousMonth = if (firstDayOfWeek == 1) 0 else firstDayOfWeek - 1

    // Calcular días del mes siguiente que se muestran
    val lastDayOfWeek = lastDayOfMonth.dayOfWeek.value
    val daysFromNextMonth = if (lastDayOfWeek == 7) 0 else 7 - lastDayOfWeek

    val days = mutableListOf<CalendarDay>()

    // Días del mes anterior
    val previousMonth = yearMonth.minusMonths(1)
    for (i in daysFromPreviousMonth downTo 1) {
        val date = previousMonth.atEndOfMonth().minusDays(i.toLong() - 1)
        days.add(createCalendarDay(date, yearMonth, today, periodDays, ovulationDate, fertileWindowStart, fertileWindowEnd, daysWithSymptoms))
    }

    // Días del mes actual
    for (i in 1..lastDayOfMonth.dayOfMonth) {
        val date = yearMonth.atDay(i)
        days.add(createCalendarDay(date, yearMonth, today, periodDays, ovulationDate, fertileWindowStart, fertileWindowEnd, daysWithSymptoms))
    }

    // Días del mes siguiente
    val nextMonth = yearMonth.plusMonths(1)
    for (i in 1..daysFromNextMonth) {
        val date = nextMonth.atDay(i)
        days.add(createCalendarDay(date, yearMonth, today, periodDays, ovulationDate, fertileWindowStart, fertileWindowEnd, daysWithSymptoms))
    }

    return days
}

/**
 * Crea un CalendarDay con todas sus propiedades
 */
private fun createCalendarDay(
    date: LocalDate,
    currentMonth: YearMonth,
    today: LocalDate,
    periodDays: Set<LocalDate>,
    ovulationDate: LocalDate?,
    fertileWindowStart: LocalDate?,
    fertileWindowEnd: LocalDate?,
    daysWithSymptoms: Set<LocalDate>
): CalendarDay {
    val isCurrentMonth = date.year == currentMonth.year && date.month == currentMonth.month
    val isToday = date == today
    val isPeriodDay = periodDays.contains(date)
    val isOvulationDay = date == ovulationDate
    val isFertileDay = fertileWindowStart != null && fertileWindowEnd != null &&
            !date.isBefore(fertileWindowStart) && !date.isAfter(fertileWindowEnd)
    val hasSymptoms = daysWithSymptoms.contains(date)
    val isFuture = date.isAfter(today)

    return CalendarDay(
        date = date,
        isCurrentMonth = isCurrentMonth,
        isToday = isToday,
        isPeriodDay = isPeriodDay,
        isOvulationDay = isOvulationDay,
        isFertileDay = isFertileDay,
        hasSymptoms = hasSymptoms,
        isFuture = isFuture
    )
}