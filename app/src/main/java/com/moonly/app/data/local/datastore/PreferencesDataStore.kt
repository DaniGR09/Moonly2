package com.moonly.app.data.local.datastore

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
import javax.inject.Singleton

// Extension para crear DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "moonly_preferences")

/**
 * Maneja el almacenamiento local usando DataStore
 */
@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // ==================== AUTH ====================

    /**
     * Guarda el access token
     */
    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.ACCESS_TOKEN] = token
        }
    }

    /**
     * Obtiene el access token
     */
    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.ACCESS_TOKEN]
        }
    }

    /**
     * Guarda el refresh token
     */
    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.REFRESH_TOKEN] = token
        }
    }

    /**
     * Obtiene el refresh token
     */
    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.REFRESH_TOKEN]
        }
    }

    /**
     * Guarda el user ID
     */
    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_ID] = userId
        }
    }

    /**
     * Obtiene el user ID
     */
    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.USER_ID]
        }
    }

    /**
     * Guarda el email del usuario
     */
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_EMAIL] = email
        }
    }

    /**
     * Obtiene el email del usuario
     */
    fun getUserEmail(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.USER_EMAIL]
        }
    }

    /**
     * Guarda el estado de login
     */
    suspend fun saveIsLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.IS_LOGGED_IN] = isLoggedIn
        }
    }

    /**
     * Obtiene el estado de login
     */
    fun getIsLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.IS_LOGGED_IN] ?: false
        }
    }

    /**
     * Guarda el estado de verificación de email
     */
    suspend fun saveIsEmailVerified(isVerified: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.IS_EMAIL_VERIFIED] = isVerified
        }
    }

    /**
     * Obtiene el estado de verificación de email
     */
    fun getIsEmailVerified(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.IS_EMAIL_VERIFIED] ?: false
        }
    }

    // ==================== ONBOARDING ====================

    /**
     * Guarda si el usuario completó el onboarding
     */
    suspend fun saveHasCompletedOnboarding(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.HAS_COMPLETED_ONBOARDING] = completed
        }
    }

    /**
     * Obtiene si el usuario completó el onboarding
     */
    fun getHasCompletedOnboarding(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.HAS_COMPLETED_ONBOARDING] ?: false
        }
    }

    // ==================== FCM ====================

    /**
     * Guarda el FCM token
     */
    suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.FCM_TOKEN] = token
        }
    }

    /**
     * Obtiene el FCM token
     */
    fun getFcmToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.FCM_TOKEN]
        }
    }

    // ==================== SETTINGS ====================

    /**
     * Guarda el modo de tema
     */
    suspend fun saveThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME_MODE] = mode
        }
    }

    /**
     * Obtiene el modo de tema
     */
    fun getThemeMode(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.THEME_MODE] ?: "system"
        }
    }

    // ==================== CLEAR ====================

    /**
     * Limpia todos los datos (logout)
     */
    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    /**
     * Limpia solo datos de sesión (mantiene onboarding y settings)
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferenceKeys.ACCESS_TOKEN)
            preferences.remove(PreferenceKeys.REFRESH_TOKEN)
            preferences.remove(PreferenceKeys.USER_ID)
            preferences.remove(PreferenceKeys.USER_EMAIL)
            preferences.remove(PreferenceKeys.IS_LOGGED_IN)
            preferences.remove(PreferenceKeys.IS_EMAIL_VERIFIED)
        }
    }
}