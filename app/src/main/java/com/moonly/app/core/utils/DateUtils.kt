package com.moonly.app.core.utils

import com.moonly.app.core.constants.AppConstants
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

object DateUtils {

    private val apiDateFormatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT_API)
    private val displayDateFormatter = DateTimeFormatter.ofPattern(
        AppConstants.DATE_FORMAT_DISPLAY,
        Locale("es", "ES")
    )
    private val monthYearFormatter = DateTimeFormatter.ofPattern(
        AppConstants.DATE_FORMAT_MONTH_YEAR,
        Locale("es", "ES")
    )
    private val apiTimeFormatter = DateTimeFormatter.ofPattern(AppConstants.TIME_FORMAT_API)
    private val displayTimeFormatter = DateTimeFormatter.ofPattern(
        AppConstants.TIME_FORMAT_DISPLAY,
        Locale("es", "ES")
    )

    /**
     * Convierte LocalDate a String formato API (yyyy-MM-dd)
     */
    fun LocalDate.toApiFormat(): String {
        return this.format(apiDateFormatter)
    }

    /**
     * Convierte String formato API a LocalDate
     */
    fun String.toLocalDate(): LocalDate? {
        return try {
            LocalDate.parse(this, apiDateFormatter)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Convierte LocalDate a String formato display (dd/MM/yyyy)
     */
    fun LocalDate.toDisplayFormat(): String {
        return this.format(displayDateFormatter)
    }

    /**
     * Convierte LocalDate a String formato mes-año (Diciembre 2024)
     */
    fun LocalDate.toMonthYearFormat(): String {
        return this.format(monthYearFormatter).replaceFirstChar { it.uppercase() }
    }

    /**
     * Convierte LocalTime a String formato API (HH:mm:ss)
     */
    fun LocalTime.toApiFormat(): String {
        return this.format(apiTimeFormatter)
    }

    /**
     * Convierte String formato API a LocalTime
     */
    fun String.toLocalTime(): LocalTime? {
        return try {
            LocalTime.parse(this, apiTimeFormatter)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Convierte LocalTime a String formato display (02:30 PM)
     */
    fun LocalTime.toDisplayFormat(): String {
        return this.format(displayTimeFormatter)
    }

    /**
     * Obtiene fecha actual
     */
    fun getCurrentDate(): LocalDate {
        return LocalDate.now()
    }

    /**
     * Obtiene hora actual
     */
    fun getCurrentTime(): LocalTime {
        return LocalTime.now()
    }

    /**
     * Calcula días entre dos fechas
     */
    fun daysBetween(startDate: LocalDate, endDate: LocalDate): Long {
        return ChronoUnit.DAYS.between(startDate, endDate)
    }

    /**
     * Verifica si una fecha es hoy
     */
    fun LocalDate.isToday(): Boolean {
        return this == getCurrentDate()
    }

    /**
     * Verifica si una fecha está en el pasado
     */
    fun LocalDate.isPast(): Boolean {
        return this.isBefore(getCurrentDate())
    }

    /**
     * Verifica si una fecha está en el futuro
     */
    fun LocalDate.isFuture(): Boolean {
        return this.isAfter(getCurrentDate())
    }

    /**
     * Obtiene el primer día del mes
     */
    fun LocalDate.getFirstDayOfMonth(): LocalDate {
        return this.withDayOfMonth(1)
    }

    /**
     * Obtiene el último día del mes
     */
    fun LocalDate.getLastDayOfMonth(): LocalDate {
        return this.withDayOfMonth(this.lengthOfMonth())
    }

    /**
     * Obtiene nombre del día de la semana
     */
    fun LocalDate.getDayOfWeekName(): String {
        val formatter = DateTimeFormatter.ofPattern("EEEE", Locale("es", "ES"))
        return this.format(formatter).replaceFirstChar { it.uppercase() }
    }

    /**
     * Obtiene nombre corto del día de la semana (L, M, X, J, V, S, D)
     */
    fun LocalDate.getDayOfWeekShort(): String {
        val formatter = DateTimeFormatter.ofPattern("EEEEE", Locale("es", "ES"))
        return this.format(formatter).uppercase()
    }

    /**
     * Obtiene todos los días del mes
     */
    fun LocalDate.getDaysInMonth(): List<LocalDate> {
        val firstDay = this.getFirstDayOfMonth()
        val lastDay = this.getLastDayOfMonth()
        val days = mutableListOf<LocalDate>()

        var currentDay = firstDay
        while (!currentDay.isAfter(lastDay)) {
            days.add(currentDay)
            currentDay = currentDay.plusDays(1)
        }

        return days
    }

    /**
     * Agrega días a una fecha
     */
    fun LocalDate.addDays(days: Long): LocalDate {
        return this.plusDays(days)
    }

    /**
     * Resta días a una fecha
     */
    fun LocalDate.subtractDays(days: Long): LocalDate {
        return this.minusDays(days)
    }
}