package com.moonly.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.repository.CycleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

/**
 * UI State para EditPeriodScreen
 */
data class EditPeriodUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDays: Set<LocalDate> = emptySet(),
    val originalPeriodDays: Set<LocalDate> = emptySet(),
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class EditPeriodViewModel @Inject constructor(
    private val cycleRepository: CycleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditPeriodUiState())
    val uiState: StateFlow<EditPeriodUiState> = _uiState.asStateFlow()

    init {
        loadExistingPeriodDays()
    }

    /**
     * Carga los días de período existentes
     */
    private fun loadExistingPeriodDays() {
        viewModelScope.launch {
            // Cargar datos de 3 meses (anterior, actual, siguiente)
            val month = _uiState.value.currentMonth
            val startDate = month.minusMonths(1).atDay(1)
            val endDate = month.plusMonths(1).atEndOfMonth()

            cycleRepository.getPeriodDays(
                startDate.toString(),
                endDate.toString()
            ).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val periodDays = result.data
                            .filter { it.isPeriodDay }
                            .map { LocalDate.parse(it.periodDate) }
                            .toSet()

                        _uiState.update {
                            it.copy(
                                selectedDays = periodDays,
                                originalPeriodDays = periodDays
                            )
                        }
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
     * Alterna la selección de un día
     */
    fun toggleDay(date: LocalDate) {
        val currentSelected = _uiState.value.selectedDays.toMutableSet()

        if (currentSelected.contains(date)) {
            currentSelected.remove(date)
        } else {
            currentSelected.add(date)
        }

        _uiState.update { it.copy(selectedDays = currentSelected) }
    }

    /**
     * Navega al mes anterior
     */
    fun navigateToPreviousMonth() {
        val newMonth = _uiState.value.currentMonth.minusMonths(1)
        _uiState.update { it.copy(currentMonth = newMonth) }
        loadMoreMonthsIfNeeded()
    }

    /**
     * Navega al mes siguiente
     */
    fun navigateToNextMonth() {
        val newMonth = _uiState.value.currentMonth.plusMonths(1)
        _uiState.update { it.copy(currentMonth = newMonth) }
        loadMoreMonthsIfNeeded()
    }

    /**
     * Carga más meses si es necesario
     */
    private fun loadMoreMonthsIfNeeded() {
        viewModelScope.launch {
            val month = _uiState.value.currentMonth
            val startDate = month.minusMonths(1).atDay(1)
            val endDate = month.plusMonths(1).atEndOfMonth()

            cycleRepository.getPeriodDays(
                startDate.toString(),
                endDate.toString()
            ).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val periodDays = result.data
                            .filter { it.isPeriodDay }
                            .map { LocalDate.parse(it.periodDate) }
                            .toSet()

                        // Mezclar con los días ya seleccionados
                        val updatedSelection = _uiState.value.selectedDays + periodDays
                        _uiState.update { it.copy(selectedDays = updatedSelection) }
                    }
                    is NetworkResult.Error -> {}
                    is NetworkResult.Loading -> {}
                }
            }
        }
    }

    /**
     * Guarda los cambios en los días de período
     */
    fun savePeriodDays() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val selectedDays = _uiState.value.selectedDays
            val originalDays = _uiState.value.originalPeriodDays

            // Días a agregar (nuevos)
            val daysToAdd = selectedDays - originalDays

            // Días a quitar (desmarcados)
            val daysToRemove = originalDays - selectedDays

            try {
                // Agregar días nuevos
                if (daysToAdd.isNotEmpty()) {
                    val dates = daysToAdd.map { it.toString() }
                    cycleRepository.addPeriodDaysBatch(dates, isPeriodDay = true)
                        .collect { result ->
                            when (result) {
                                is NetworkResult.Error -> {
                                    _uiState.update {
                                        it.copy(
                                            isSaving = false,
                                            errorMessage = result.message
                                        )
                                    }
                                    return@collect
                                }
                                else -> {}
                            }
                        }
                }

                // Quitar días desmarcados
                if (daysToRemove.isNotEmpty()) {
                    val dates = daysToRemove.map { it.toString() }
                    cycleRepository.addPeriodDaysBatch(dates, isPeriodDay = false)
                        .collect { result ->
                            when (result) {
                                is NetworkResult.Error -> {
                                    _uiState.update {
                                        it.copy(
                                            isSaving = false,
                                            errorMessage = result.message
                                        )
                                    }
                                    return@collect
                                }
                                else -> {}
                            }
                        }
                }

                // Éxito
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveSuccess = true
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Error al guardar"
                    )
                }
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