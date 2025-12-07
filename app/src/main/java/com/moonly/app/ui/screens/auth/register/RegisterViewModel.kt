package com.moonly.app.ui.screens.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val isEmailVerified: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val TAG = "RegisterViewModel"

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null,
            generalError = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null,
            generalError = null
        )
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null,
            generalError = null
        )
    }

    fun onRegisterClick() {
        if (!validateInputs()) return

        viewModelScope.launch {
            authRepository.signUp(
                email = _uiState.value.email.trim(),
                password = _uiState.value.password
            ).collect { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            generalError = null
                        )
                        Log.d(TAG, "⏳ Cargando...")
                    }

                    is NetworkResult.Success -> {
                        Log.d(TAG, "✅ Registro exitoso")

                        // Verificar si el email está confirmado
                        val isVerified = result.data.user.emailConfirmedAt != null

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            registerSuccess = true,
                            isEmailVerified = isVerified
                        )

                        Log.d(TAG, "📧 Email verificado: $isVerified")
                    }

                    is NetworkResult.Error -> {
                        Log.e(TAG, "❌ Error en registro: ${result.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            generalError = parseErrorMessage(result.message)
                        )
                    }
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        // Validar email vacío
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(
                emailError = "El correo es requerido"
            )
            return false
        }

        // Validar formato de email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = _uiState.value.copy(
                emailError = "Ingresa un correo válido"
            )
            return false
        }

        // Validar contraseña vacía
        if (password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                passwordError = "La contraseña es requerida"
            )
            return false
        }

        // Validar longitud de contraseña
        if (password.length < 8) {
            _uiState.value = _uiState.value.copy(
                passwordError = "La contraseña debe tener al menos 8 caracteres"
            )
            return false
        }

        // Validar confirmación vacía
        if (confirmPassword.isBlank()) {
            _uiState.value = _uiState.value.copy(
                confirmPasswordError = "Confirma tu contraseña"
            )
            return false
        }

        // Validar que las contraseñas coincidan
        if (password != confirmPassword) {
            _uiState.value = _uiState.value.copy(
                confirmPasswordError = "Las contraseñas no coinciden"
            )
            return false
        }

        return true
    }

    private fun parseErrorMessage(message: String): String {
        return when {
            message.contains("already registered", ignoreCase = true) ->
                "Este correo ya está registrado"
            message.contains("invalid email", ignoreCase = true) ->
                "Correo electrónico inválido"
            message.contains("weak password", ignoreCase = true) ->
                "La contraseña es muy débil"
            else -> "Error al crear la cuenta. Inténtalo de nuevo"
        }
    }
}