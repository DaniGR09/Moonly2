package com.moonly.app.ui.screens.profile.caremethod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.dto.care_methods.CareMethodCreateRequest
import com.moonly.app.data.repository.CareMethodsRepository
import com.moonly.app.domain.model.CareMethodType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI State para CareMethodsScreen
 */
data class CareMethodsUiState(
    val selectedMethod: CareMethodType? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class CareMethodsViewModel @Inject constructor(
    private val careMethodsRepository: CareMethodsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CareMethodsUiState())
    val uiState: StateFlow<CareMethodsUiState> = _uiState.asStateFlow()

    init {
        loadCurrentMethod()
    }

    /**
     * Carga el método de cuidado actual
     */
    private fun loadCurrentMethod() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            careMethodsRepository.getCareMethods(activeOnly = true).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val currentMethod = result.data.firstOrNull()
                        val type = currentMethod?.let {
                            CareMethodType.fromApiValue(it.methodType)
                        }

                        _uiState.update {
                            it.copy(
                                selectedMethod = type,
                                isLoading = false
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message
                            )
                        }
                    }
                    is NetworkResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    /**
     * Selecciona un nuevo método de cuidado
     */
    fun selectMethod(type: CareMethodType) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Paso 1: Eliminar método actual si existe
            careMethodsRepository.getCareMethods(activeOnly = true).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        // Eliminar todos los métodos existentes
                        result.data.forEach { method ->
                            careMethodsRepository.deleteCareMethod(method.id).collect { deleteResult ->
                                // Ignorar resultado del delete
                            }
                        }

                        // Paso 2: Crear el nuevo método
                        createNewMethod(type)
                    }
                    is NetworkResult.Error -> {
                        // Si no hay métodos existentes, crear directamente
                        createNewMethod(type)
                    }
                    is NetworkResult.Loading -> {}
                }
            }
        }
    }

    /**
     * Crea un nuevo método de cuidado
     */
    private suspend fun createNewMethod(type: CareMethodType) {
        val request = CareMethodCreateRequest(
            methodType = type.toApiValue(),
            reminderEnabled = false,
            changeIntervalHours = type.getRecommendedIntervalHours()
        )

        careMethodsRepository.createCareMethod(request).collect { createResult ->
            when (createResult) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            selectedMethod = type,
                            isLoading = false
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = createResult.message
                        )
                    }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}