package com.moonly.app.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.core.utils.ValidationUtils
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.repository.AuthRepository
import com.moonly.app.data.local.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager // ✅ Cambiado a PreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, generalError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, generalError = null) }
    }

    fun onLoginClick() {
        // Validación
        val emailError = ValidationUtils.getEmailError(_uiState.value.email)
        val passwordError = ValidationUtils.getPasswordError(_uiState.value.password)

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        // Iniciar login
        viewModelScope.launch {
            authRepository.signIn(_uiState.value.email, _uiState.value.password)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            _uiState.update { it.copy(isLoading = true, generalError = null) }
                        }

                        is NetworkResult.Success -> {
                            val session = result.data.session
                            val user = result.data.user

                            // ✅ Guardar datos en PreferenceManager (el mismo que usa AuthInterceptor)
                            preferenceManager.saveAuthData(
                                accessToken = session.accessToken ?: "",
                                refreshToken = session.refreshToken,
                                userId = user.id,
                                userEmail = user.email
                            )

                            preferenceManager.setOnboardingCompleted(true)

                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    loginSuccess = true
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