package com.moonly.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonly.app.data.local.datastore.PreferencesDataStore
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.repository.CycleRepository
import com.moonly.app.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

/**
 * Estados del panel de registro
 */
enum class BottomSheetState {
    MINIMIZED,  // Panel minimizado (altura pequeña)
    EXPANDED    // Panel expandido (casi toda la pantalla)
}

/**
 * UI State para HomeScreen
 */
data class HomeUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate? = null,
    val periodDays: Set<LocalDate> = emptySet(),
    val ovulationDate: LocalDate? = null,
    val fertileWindowStart: LocalDate? = null,
    val fertileWindowEnd: LocalDate? = null,
    val daysWithSymptoms: Set<LocalDate> = emptySet(),
    val currentSymptoms: DailySymptoms? = null,
    val bottomSheetState: BottomSheetState = BottomSheetState.MINIMIZED,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cycleRepository: CycleRepository,
    val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMonthData()
        loadOvulationInfo()
    }

    // ==================== NAVEGACIÓN DE MES ====================

    /**
     * Cambia al mes anterior
     */
    fun navigateToPreviousMonth() {
        val newMonth = _uiState.value.currentMonth.minusMonths(1)
        _uiState.update { it.copy(currentMonth = newMonth) }
        loadMonthData()
    }

    /**
     * Cambia al mes siguiente
     */
    fun navigateToNextMonth() {
        val newMonth = _uiState.value.currentMonth.plusMonths(1)
        _uiState.update { it.copy(currentMonth = newMonth) }
        loadMonthData()
    }

    // ==================== SELECCIÓN DE DÍA ====================

    /**
     * Selecciona un día del calendario
     * Regla: No se pueden seleccionar días futuros
     */
    fun selectDate(date: LocalDate) {
        if (date.isAfter(LocalDate.now())) {
            _uiState.update { it.copy(errorMessage = "No puedes registrar días futuros") }
            return
        }

        _uiState.update {
            it.copy(
                selectedDate = date,
                bottomSheetState = BottomSheetState.MINIMIZED
            )
        }
        loadSymptomsForDate(date)
    }

    /**
     * Limpia la selección de día
     */
    fun clearDateSelection() {
        _uiState.update {
            it.copy(
                selectedDate = null,
                currentSymptoms = null,
                bottomSheetState = BottomSheetState.MINIMIZED
            )
        }
    }

    // ==================== BOTTOM SHEET ====================

    /**
     * Expande el panel de registro
     */
    fun expandBottomSheet() {
        _uiState.update { it.copy(bottomSheetState = BottomSheetState.EXPANDED) }
    }

    /**
     * Minimiza el panel de registro
     */
    fun minimizeBottomSheet() {
        _uiState.update { it.copy(bottomSheetState = BottomSheetState.MINIMIZED) }
    }

    /**
     * Alterna el estado del bottom sheet
     */
    fun toggleBottomSheet() {
        val newState = if (_uiState.value.bottomSheetState == BottomSheetState.MINIMIZED) {
            BottomSheetState.EXPANDED
        } else {
            BottomSheetState.MINIMIZED
        }
        _uiState.update { it.copy(bottomSheetState = newState) }
    }

    // ==================== CARGA DE DATOS ====================

    /**
     * Carga los datos del mes actual (días de periodo y síntomas)
     */
    private fun loadMonthData() {
        val month = _uiState.value.currentMonth
        val startDate = month.atDay(1)
        val endDate = month.atEndOfMonth()

        viewModelScope.launch {
            // NO mostrar loading al cambiar de mes (mejora UX)

            // Cargar días de periodo
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

                        _uiState.update { it.copy(periodDays = periodDays) }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update { it.copy(errorMessage = result.message) }
                    }
                    is NetworkResult.Loading -> {
                        // Ignorar loading para no mostrar pantalla gris
                    }
                }
            }
        }
    }

    /**
     * Carga información de ovulación
     */
    private fun loadOvulationInfo() {
        viewModelScope.launch {
            cycleRepository.getOvulationInfo().collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val info = result.data.toOvulationInfo()
                        _uiState.update {
                            it.copy(
                                ovulationDate = info.estimatedOvulationDate,
                                fertileWindowStart = info.fertileWindowStart,
                                fertileWindowEnd = info.fertileWindowEnd
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        // Ignorar error silenciosamente (ovulación es opcional)
                    }
                    is NetworkResult.Loading -> {}
                }
            }
        }
    }

    /**
     * Carga síntomas de un día específico
     */
    private fun loadSymptomsForDate(date: LocalDate) {
        viewModelScope.launch {
            cycleRepository.getDailySymptoms(date.toString()).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val symptoms = result.data.toDailySymptoms()
                        _uiState.update { it.copy(currentSymptoms = symptoms) }
                    }
                    is NetworkResult.Error -> {
                        // Si no hay síntomas, es normal
                        _uiState.update { it.copy(currentSymptoms = null) }
                    }
                    is NetworkResult.Loading -> {}
                }
            }
        }
    }

    // ==================== REGISTRO DE SÍNTOMAS ====================

    /**
     * Actualiza una emoción
     */
    fun updateEmotion(emotion: Emotion) {
        val currentEmotions = _uiState.value.currentSymptoms?.emotions?.toMutableList() ?: mutableListOf()

        if (currentEmotions.contains(emotion)) {
            currentEmotions.remove(emotion)
        } else {
            currentEmotions.add(emotion)
        }

        updateSymptoms(emotions = currentEmotions)
    }

    /**
     * Actualiza cantidad de sangrado
     */
    fun updateBleedingAmount(amount: BleedingAmount?) {
        updateSymptoms(bleedingAmount = amount)
    }

    /**
     * Actualiza color de sangrado
     */
    fun updateBleedingColor(color: BleedingColor?) {
        updateSymptoms(bleedingColor = color)
    }

    /**
     * Actualiza color de flujo
     */
    fun updateFlowColor(color: FlowColor?) {
        updateSymptoms(flowColor = color)
    }

    /**
     * Actualiza antojos
     */
    fun updateCravings(cravings: String?) {
        updateSymptoms(cravings = cravings)
    }

    /**
     * Actualiza nivel de dolor
     */
    fun updatePainLevel(level: Int?) {
        updateSymptoms(painLevel = level)
    }

    /**
     * Método privado para actualizar síntomas
     */
    private fun updateSymptoms(
        bleedingAmount: BleedingAmount? = _uiState.value.currentSymptoms?.bleedingAmount,
        bleedingColor: BleedingColor? = _uiState.value.currentSymptoms?.bleedingColor,
        flowColor: FlowColor? = _uiState.value.currentSymptoms?.flowColor,
        cravings: String? = _uiState.value.currentSymptoms?.cravings,
        painLevel: Int? = _uiState.value.currentSymptoms?.painLevel,
        emotions: List<Emotion>? = _uiState.value.currentSymptoms?.emotions
    ) {
        val selectedDate = _uiState.value.selectedDate ?: return

        viewModelScope.launch {
            val request = com.moonly.app.data.remote.dto.cycle.DailySymptomsRequest(
                symptomDate = selectedDate.toString(),
                bleedingAmount = bleedingAmount?.toApiValue(),
                bleedingColor = bleedingColor?.toApiValue(),
                flowColor = flowColor?.toApiValue(),
                cravings = cravings,
                painLevel = painLevel,
                emotions = Emotion.toJsonArray(emotions)
            )

            cycleRepository.addDailySymptoms(request).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val symptoms = result.data.toDailySymptoms()
                        _uiState.update {
                            it.copy(
                                currentSymptoms = symptoms,
                                daysWithSymptoms = it.daysWithSymptoms + selectedDate
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
     * Limpia el mensaje de error
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}