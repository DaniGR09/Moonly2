package com.moonly.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.repository.*
import com.moonly.app.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI State para ProfileMenuScreen
 */
data class ProfileMenuUiState(
    val user: User? = null,
    val activeContraceptives: Int = 0,
    val activeMedicalConditions: Int = 0,
    val currentCareMethod: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class ProfileMenuViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val contraceptivesRepository: ContraceptivesRepository,
    private val medicalConditionsRepository: MedicalConditionsRepository,
    private val careMethodsRepository: CareMethodsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileMenuUiState())
    val uiState: StateFlow<ProfileMenuUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    /**
     * Carga todos los datos del perfil
     */
    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Cargar perfil
            profileRepository.getProfile().collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val user = result.data.toUser()
                        _uiState.update { it.copy(user = user) }
                    }
                    else -> {}
                }
            }

            // Cargar anticonceptivos activos
            contraceptivesRepository.getContraceptives(activeOnly = true).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.update { it.copy(activeContraceptives = result.data.size) }
                    }
                    else -> {}
                }
            }

            // Cargar condiciones médicas activas
            medicalConditionsRepository.getMedicalConditions(activeOnly = true).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.update { it.copy(activeMedicalConditions = result.data.size) }
                    }
                    else -> {}
                }
            }

            // Cargar método de cuidado actual
            careMethodsRepository.getCareMethods(activeOnly = true).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val currentMethod = result.data.firstOrNull()
                        val methodName = currentMethod?.let {
                            CareMethodType.fromApiValue(it.methodType)?.getDisplayName()
                        }
                        _uiState.update { it.copy(currentCareMethod = methodName) }
                    }
                    else -> {}
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}