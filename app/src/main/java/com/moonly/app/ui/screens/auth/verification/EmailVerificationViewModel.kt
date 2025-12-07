package com.moonly.app.ui.screens.auth.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.data.local.datastore.PreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmailVerificationUiState(
    val message: String? = null,
    val isError: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmailVerificationUiState())
    val uiState: StateFlow<EmailVerificationUiState> = _uiState.asStateFlow()

    fun resendVerificationEmail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }

            // TODO: Implementar reenvío de correo con el backend
            // Por ahora solo simulamos
            kotlinx.coroutines.delay(1500)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    message = "Correo de verificación enviado",
                    isError = false
                )
            }
        }
    }
}