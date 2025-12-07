package com.moonly.app.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.core.utils.ValidationUtils
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val isEmailVerified: Boolean = false // ⚠️ NUEVO
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, generalError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, generalError = null) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null, generalError = null) }
    }

    fun onRegisterClick() {
        // Validar campos
        val emailError = ValidationUtils.getEmailError(_uiState.value.email)
        val passwordError = ValidationUtils.getPasswordError(_uiState.value.password)
        val confirmPasswordError = ValidationUtils.getConfirmPasswordError(
            _uiState.value.password,
            _uiState.value.confirmPassword
        )

        if (emailError != null || passwordError != null || confirmPasswordError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            return
        }

        // Llamar al repositorio
        viewModelScope.launch {
            authRepository.signUp(_uiState.value.email, _uiState.value.password)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            _uiState.update { it.copy(isLoading = true, generalError = null) }
                        }
                        is NetworkResult.Success -> {
                            // ⚠️ NUEVO: Verificar si el email está confirmado
                            val isVerified = result.data.user.emailConfirmedAt != null

                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    registerSuccess = true,
                                    isEmailVerified = isVerified
                                )
                            }
                        }
                        is NetworkResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    generalError = result.message
                                )
                            }
                        }
                    }
                }
        }
    }
}