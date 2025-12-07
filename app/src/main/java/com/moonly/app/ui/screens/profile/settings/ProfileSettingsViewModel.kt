package com.moonly.app.ui.screens.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.data.local.datastore.PreferencesDataStore
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.repository.AuthRepository
import com.moonly.app.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI State para ProfileSettingsScreen
 */
data class ProfileSettingsUiState(
    val email: String? = null,
    val nickname: String? = null,
    val birthYear: Int? = null,
    val isLoading: Boolean = false,
    val logoutSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSettingsUiState())
    val uiState: StateFlow<ProfileSettingsUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    /**
     * Carga el perfil del usuario
     */
    private fun loadProfile() {
        viewModelScope.launch {
            profileRepository.getProfile().collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val user = result.data
                        _uiState.update {
                            it.copy(
                                email = user.email,
                                nickname = user.nickname,
                                birthYear = user.birthYear
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update { it.copy(errorMessage = result.message) }
                    }
                    is NetworkResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    /**
     * Actualiza el apodo
     */
    fun updateNickname(nickname: String) {
        viewModelScope.launch {
            profileRepository.updateProfile(
                nickname = nickname.ifBlank { null },
                birthYear = _uiState.value.birthYear
            ).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.update { it.copy(nickname = nickname) }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update { it.copy(errorMessage = result.message) }
                    }
                    is NetworkResult.Loading -> {}
                }
            }
        }
    }

    /**
     * Actualiza el año de nacimiento
     */
    fun updateBirthYear(year: Int) {
        viewModelScope.launch {
            profileRepository.updateProfile(
                nickname = _uiState.value.nickname,
                birthYear = year
            ).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.update { it.copy(birthYear = year) }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update { it.copy(errorMessage = result.message) }
                    }
                    is NetworkResult.Loading -> {}
                }
            }
        }
    }

    /**
     * Cambia la contraseña
     */
    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            // Primero validar credenciales actuales
            authRepository.signIn(
                _uiState.value.email ?: "",
                currentPassword
            ).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        // Si las credenciales son correctas, actualizar contraseña
                        val userId = result.data.user.id
                        authRepository.updatePassword(userId, newPassword).collect { updateResult -> //error
                            when (updateResult) {
                                is NetworkResult.Success -> { //error
                                    // Éxito
                                }
                                is NetworkResult.Error -> {
                                    _uiState.update { it.copy(errorMessage = updateResult.message) }
                                }
                                is NetworkResult.Loading -> {}
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update { it.copy(errorMessage = "Contraseña actual incorrecta") }
                    }
                    is NetworkResult.Loading -> {}
                }
            }
        }
    }

    /**
     * Elimina la cuenta
     */
    fun deleteAccount() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            profileRepository.deleteAccount().collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        // Limpiar datos locales
                        preferencesDataStore.clearAll()
                        _uiState.update { it.copy(isLoading = false, logoutSuccess = true) }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message
                            )
                        }
                    }
                    is NetworkResult.Loading -> {}
                }
            }
        }
    }

    /**
     * Cierra sesión
     */
    fun logout() {
        viewModelScope.launch {
            preferencesDataStore.clearAll()
            _uiState.update { it.copy(logoutSuccess = true) }
        }
    }
}