package com.moonly.app.ui.screens.auth.recovery

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

data class PasswordRecoveryUiState(
    val email: String = "",
    val emailError: String? = null,
    val generalError: String? = null,
    val successMessage: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordRecoveryUiState())
    val uiState: StateFlow<PasswordRecoveryUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, generalError = null, successMessage = null) }
    }

    fun onSendClick() {
        // Validar email
        val emailError = ValidationUtils.getEmailError(_uiState.value.email)

        if (emailError != null) {
            _uiState.update { it.copy(emailError = emailError) }
            return
        }

        // Llamar al repositorio
        viewModelScope.launch {
            authRepository.resetPassword(_uiState.value.email)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            _uiState.update { it.copy(isLoading = true, generalError = null, successMessage = null) }
                        }
                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    successMessage = result.data
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