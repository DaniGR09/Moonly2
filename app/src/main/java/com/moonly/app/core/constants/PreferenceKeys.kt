package com.moonly.app.core.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    // Auth
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val USER_ID = stringPreferencesKey("user_id")
    val USER_EMAIL = stringPreferencesKey("user_email")
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val IS_EMAIL_VERIFIED = booleanPreferencesKey("is_email_verified")

    // Onboarding
    val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")

    // FCM
    val FCM_TOKEN = stringPreferencesKey("fcm_token")

    // Settings
    val THEME_MODE = stringPreferencesKey("theme_mode") // "light", "dark", "system"
}