package com.moonly.app.core.utils

import com.moonly.app.core.constants.AppConstants
import java.util.regex.Pattern

object ValidationUtils {

    private val EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    /**
     * Valida formato de email
     */
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && EMAIL_PATTERN.matcher(email).matches()
    }

    /**
     * Valida contraseña (mínimo 8 caracteres)
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= AppConstants.MIN_PASSWORD_LENGTH
    }

    /**
     * Valida que las contraseñas coincidan
     */
    fun passwordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    /**
     * Valida año de nacimiento
     */
    fun isValidBirthYear(year: Int?): Boolean {
        if (year == null) return false
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        return year in AppConstants.MIN_BIRTH_YEAR..currentYear
    }

    /**
     * Valida duración del ciclo
     */
    fun isValidCycleLength(length: Int): Boolean {
        return length in AppConstants.MIN_CYCLE_LENGTH..AppConstants.MAX_CYCLE_LENGTH
    }

    /**
     * Valida duración del periodo
     */
    fun isValidPeriodLength(length: Int): Boolean {
        return length in AppConstants.MIN_PERIOD_LENGTH..AppConstants.MAX_PERIOD_LENGTH
    }

    /**
     * Valida nivel de dolor (0-10)
     */
    fun isValidPainLevel(level: Int): Boolean {
        return level in AppConstants.MIN_PAIN_LEVEL..AppConstants.MAX_PAIN_LEVEL
    }

    /**
     * Valida intervalo de cambio (1-24 horas)
     */
    fun isValidChangeInterval(hours: Int): Boolean {
        return hours in AppConstants.MIN_CHANGE_INTERVAL..AppConstants.MAX_CHANGE_INTERVAL
    }

    /**
     * Obtiene mensaje de error para email
     */
    fun getEmailError(email: String): String? {
        return when {
            email.isBlank() -> "El correo es requerido"
            !isValidEmail(email) -> "Correo inválido"
            else -> null
        }
    }

    /**
     * Obtiene mensaje de error para contraseña
     */
    fun getPasswordError(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña es requerida"
            !isValidPassword(password) -> "La contraseña debe tener al menos ${AppConstants.MIN_PASSWORD_LENGTH} caracteres"
            else -> null
        }
    }

    /**
     * Obtiene mensaje de error para confirmar contraseña
     */
    fun getConfirmPasswordError(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isBlank() -> "Confirma tu contraseña"
            !passwordsMatch(password, confirmPassword) -> "Las contraseñas no coinciden"
            else -> null
        }
    }
}