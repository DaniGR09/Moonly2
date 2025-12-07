package com.moonly.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.moonly.app.core.constants.PreferenceKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "moonly_prefs")

class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val dataStore = context.dataStore

    // ==================== AUTH ====================

    val accessToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.ACCESS_TOKEN]
    }

    val refreshToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.REFRESH_TOKEN]
    }

    val userId: Flow<String?> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.USER_ID]
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.IS_LOGGED_IN] ?: false
    }

    suspend fun saveAuthData(
        accessToken: String,
        refreshToken: String?,
        userId: String,
        userEmail: String
    ) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.ACCESS_TOKEN] = accessToken
            refreshToken?.let { prefs[PreferenceKeys.REFRESH_TOKEN] = it }
            prefs[PreferenceKeys.USER_ID] = userId
            prefs[PreferenceKeys.USER_EMAIL] = userEmail
            prefs[PreferenceKeys.IS_LOGGED_IN] = true
        }
    }

    suspend fun clearAuthData() {
        dataStore.edit { prefs ->
            prefs.remove(PreferenceKeys.ACCESS_TOKEN)
            prefs.remove(PreferenceKeys.REFRESH_TOKEN)
            prefs.remove(PreferenceKeys.USER_ID)
            prefs.remove(PreferenceKeys.USER_EMAIL)
            prefs[PreferenceKeys.IS_LOGGED_IN] = false
        }
    }

    // ==================== ONBOARDING ====================

    val hasCompletedOnboarding: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.HAS_COMPLETED_ONBOARDING] ?: false
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.HAS_COMPLETED_ONBOARDING] = completed
        }
    }

    // ==================== FCM ====================

    val fcmToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.FCM_TOKEN]
    }

    suspend fun saveFcmToken(token: String) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.FCM_TOKEN] = token
        }
    }

    // ==================== SETTINGS ====================

    val themeMode: Flow<String> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.THEME_MODE] ?: "system"
    }

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.THEME_MODE] = mode
        }
    }
}
