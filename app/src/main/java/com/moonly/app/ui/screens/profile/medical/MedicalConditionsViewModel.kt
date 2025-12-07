package com.moonly.app.ui.screens.profile.medical

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.dto.medical.MedicalConditionCreateRequest
import com.moonly.app.data.repository.MedicalConditionsRepository
import com.moonly.app.domain.model.MedicalConditionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI State para MedicalConditionsScreen
 */
data class MedicalConditionsUiState(
    val selectedConditions: Set<MedicalConditionType> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class MedicalConditionsViewModel @Inject constructor(
    private val medicalConditionsRepository: MedicalConditionsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicalConditionsUiState())
    val uiState: StateFlow<MedicalConditionsUiState> = _uiState.asStateFlow()

    init {
        loadConditions()
    }

    /**
     * Carga las condiciones médicas actuales del usuario
     */
    private fun loadConditions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            medicalConditionsRepository.getMedicalConditions(activeOnly = true).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val types = result.data.map {
                            MedicalConditionType.fromApiValue(it.conditionType) ?: MedicalConditionType.OTRA
                        }.toSet()

                        _uiState.update {
                            it.copy(
                                selectedConditions = types,
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
     * Alterna una condición médica (agregar o quitar)
     */
    fun toggleCondition(type: MedicalConditionType, isChecked: Boolean) {
        viewModelScope.launch {
            if (isChecked) {
                // Agregar nueva condición
                addCondition(type)
            } else {
                // Quitar condición existente
                removeCondition(type)
            }
        }
    }

    /**
     * Agrega una nueva condición médica
     */
    private suspend fun addCondition(type: MedicalConditionType) {
        val request = MedicalConditionCreateRequest(
            conditionType = type.toApiValue()
        )

        medicalConditionsRepository.createMedicalCondition(request).collect { result ->
            when (result) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(selectedConditions = it.selectedConditions + type)
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /**
     * Elimina una condición médica existente
     */
    private suspend fun removeCondition(type: MedicalConditionType) {
        // Primero obtener todas las condiciones para encontrar el ID
        medicalConditionsRepository.getMedicalConditions(activeOnly = true).collect { result ->
            when (result) {
                is NetworkResult.Success -> {
                    // Buscar la condición que coincida con el tipo
                    val condition = result.data.find {
                        it.conditionType == type.toApiValue()
                    }

                    condition?.let {
                        // Eliminar la condición encontrada
                        medicalConditionsRepository.deleteMedicalCondition(it.id).collect { deleteResult ->
                            when (deleteResult) {
                                is NetworkResult.Success -> {
                                    _uiState.update { state ->
                                        state.copy(selectedConditions = state.selectedConditions - type)
                                    }
                                }
                                is NetworkResult.Error -> {
                                    _uiState.update { state ->
                                        state.copy(errorMessage = deleteResult.message)
                                    }
                                }
                                is NetworkResult.Loading -> {}
                            }
                        }
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message) }
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