package com.moonly.app.ui.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.core.constants.AppConstants
import com.moonly.app.data.local.datastore.PreferencesDataStore
import com.moonly.app.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            // Delay para mostrar splash con animación
            delay(AppConstants.SPLASH_DELAY)

            // Verificar estado de autenticación
            val isLoggedIn = preferencesDataStore.getIsLoggedIn().first()
            val hasCompletedOnboarding = preferencesDataStore.getHasCompletedOnboarding().first()

            // Logs para debug
            Log.d("SplashViewModel", "isLoggedIn: $isLoggedIn")
            Log.d("SplashViewModel", "hasCompletedOnboarding: $hasCompletedOnboarding")

            // Decidir a dónde navegar según el FLUJO CORRECTO
            val destination = when {
                !isLoggedIn -> {
                    Log.d("SplashViewModel", "Usuario NO logueado → Login")
                    Screen.Login.route
                }
                !hasCompletedOnboarding -> {
                    Log.d("SplashViewModel", "Usuario logueado pero sin onboarding → Onboarding")
                    Screen.Onboarding.route
                }
                else -> {
                    Log.d("SplashViewModel", "Usuario logueado y con onboarding → Home")
                    Screen.Home.route
                }
            }

            _navigateTo.value = destination
        }
    }
}