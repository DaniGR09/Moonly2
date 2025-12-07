package com.moonly.app.data.local.datastore

/**
 * Modelo de datos para preferencias de usuario
 */
data class UserPreferences(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val userId: String? = null,
    val userEmail: String? = null,
    val isLoggedIn: Boolean = false,
    val isEmailVerified: Boolean = false,
    val hasCompletedOnboarding: Boolean = false,
    val fcmToken: String? = null,
    val themeMode: String = "system" // "light", "dark", "system"
)