package com.moonly.app.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.data.local.datastore.PreferencesDataStore
import com.moonly.app.data.repository.ProfileRepository
import com.moonly.app.domain.model.CareMethodType
import com.moonly.app.domain.model.ContraceptiveType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class OnboardingUiState(
    val nickname: String = "",
    val birthYear: Int? = null,
    val lastPeriodDate: LocalDate? = null,
    val contraceptiveType: ContraceptiveType? = null,
    val careMethod: CareMethodType? = null,
    val isLoading: Boolean = false,
    val onboardingComplete: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onNicknameChange(nickname: String) {
        _uiState.update { it.copy(nickname = nickname) }
    }

    fun onBirthYearChange(year: Int?) {
        _uiState.update { it.copy(birthYear = year) }
    }

    fun onLastPeriodDateChange(date: LocalDate?) {
        _uiState.update { it.copy(lastPeriodDate = date) }
    }

    fun onContraceptiveTypeChange(type: ContraceptiveType?) {
        _uiState.update { it.copy(contraceptiveType = type) }
    }

    fun onCareMethodChange(method: CareMethodType?) {
        _uiState.update { it.copy(careMethod = method) }
    }

    fun skipOnboarding() {
        viewModelScope.launch {
            preferencesDataStore.saveHasCompletedOnboarding(true)
            _uiState.update { it.copy(onboardingComplete = true) }
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // TODO: Enviar datos al backend
            // Por ahora solo marcamos como completado

            kotlinx.coroutines.delay(1000)

            preferencesDataStore.saveHasCompletedOnboarding(true)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    onboardingComplete = true
                )
            }
        }
    }
}