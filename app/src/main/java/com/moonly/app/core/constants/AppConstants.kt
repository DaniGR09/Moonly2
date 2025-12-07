package com.moonly.app.core.constants

object AppConstants {
    // App info
    const val APP_NAME = "Moonly"
    const val APP_VERSION = "1.0.0"

    // Splash
    const val SPLASH_DELAY = 2000L // milisegundos

    // Validation
    const val MIN_PASSWORD_LENGTH = 8
    const val MAX_NICKNAME_LENGTH = 30
    const val MIN_BIRTH_YEAR = 1900
    const val MAX_BIRTH_YEAR = 2024

    // Cycle
    const val MIN_CYCLE_LENGTH = 21
    const val MAX_CYCLE_LENGTH = 45
    const val MIN_PERIOD_LENGTH = 2
    const val MAX_PERIOD_LENGTH = 10
    const val DEFAULT_CYCLE_LENGTH = 28
    const val DEFAULT_PERIOD_LENGTH = 5

    // Pain scale
    const val MIN_PAIN_LEVEL = 0
    const val MAX_PAIN_LEVEL = 10

    // Care methods
    const val MIN_CHANGE_INTERVAL = 1
    const val MAX_CHANGE_INTERVAL = 24

    // Pagination
    const val DEFAULT_PAGE_SIZE = 20
    const val MAX_PAGE_SIZE = 100

    // Date formats
    const val DATE_FORMAT_API = "yyyy-MM-dd" // 2024-12-04
    const val DATE_FORMAT_DISPLAY = "dd/MM/yyyy" // 04/12/2024
    const val DATE_FORMAT_MONTH_YEAR = "MMMM yyyy" // Diciembre 2024
    const val TIME_FORMAT_API = "HH:mm:ss" // 14:30:00
    const val TIME_FORMAT_DISPLAY = "hh:mm a" // 02:30 PM

    // FCM
    const val FCM_CHANNEL_ID = "moonly_notifications"
    const val FCM_CHANNEL_NAME = "Moonly Notifications"
    const val FCM_CHANNEL_DESCRIPTION = "Notificaciones de Moonly"
}