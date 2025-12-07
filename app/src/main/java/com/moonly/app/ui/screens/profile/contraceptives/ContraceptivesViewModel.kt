package com.moonly.app.ui.screens.profile.contraceptives

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.dto.contraceptives.ContraceptiveCreateRequest
import com.moonly.app.data.remote.dto.contraceptives.ContraceptiveUpdateRequest
import com.moonly.app.data.repository.ContraceptivesRepository
import com.moonly.app.domain.model.Contraceptive
import com.moonly.app.domain.model.ContraceptiveType
import com.moonly.app.domain.model.toContraceptive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContraceptivesUiState(
    val contraceptives: List<Contraceptive> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class ContraceptivesViewModel @Inject constructor(
    private val contraceptivesRepository: ContraceptivesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContraceptivesUiState())
    val uiState: StateFlow<ContraceptivesUiState> = _uiState.asStateFlow()

    init {
        loadContraceptives()
    }

    private fun loadContraceptives() {
        viewModelScope.launch {
            contraceptivesRepository.getContraceptives(activeOnly = true).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val contraceptives = result.data.map { it.toContraceptive() }
                        _uiState.update { it.copy(contraceptives = contraceptives) }
                    }
                    is NetworkResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    fun addContraceptive(type: ContraceptiveType, name: String?, notificationEnabled: Boolean) {
        viewModelScope.launch {
            val request = ContraceptiveCreateRequest(
                contraceptiveType = type.toApiValue(),
                contraceptiveName = name,
                notificationEnabled = notificationEnabled
            )

            contraceptivesRepository.createContraceptive(request).collect { result ->
                if (result is NetworkResult.Success) {
                    loadContraceptives()
                }
            }
        }
    }

    fun updateContraceptive(id: String, type: ContraceptiveType, name: String?, notificationEnabled: Boolean) {
        viewModelScope.launch {
            val request = ContraceptiveUpdateRequest(
                contraceptiveType = type.toApiValue(),
                contraceptiveName = name,
                notificationEnabled = notificationEnabled
            )

            contraceptivesRepository.updateContraceptive(id, request).collect { result ->
                if (result is NetworkResult.Success) {
                    loadContraceptives()
                }
            }
        }
    }

    fun deleteContraceptive(id: String) {
        viewModelScope.launch {
            contraceptivesRepository.deleteContraceptive(id).collect { result ->
                if (result is NetworkResult.Success) {
                    loadContraceptives()
                }
            }
        }
    }
}