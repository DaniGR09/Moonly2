package com.moonly.app.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moonly.app.ui.components.buttons.MoonlyButton
import com.moonly.app.ui.components.buttons.MoonlyOutlinedButton
import com.moonly.app.ui.components.dialogs.LoadingDialog
import com.moonly.app.ui.screens.onboarding.steps.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 5 }) // ✅ CAMBIO: 5 pasos
    val coroutineScope = rememberCoroutineScope()

    // Manejar navegación exitosa
    LaunchedEffect(uiState.onboardingComplete) {
        if (uiState.onboardingComplete) {
            onNavigateToHome()
        }
    }

    // Mostrar loading
    if (uiState.isLoading) {
        LoadingDialog(message = "Guardando...")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Indicador de progreso
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) { index -> // ✅ CAMBIO: 5 indicadores
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 12.dp else 8.dp)
                        .padding(horizontal = 4.dp)
                        .background(
                            color = if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                )
            }
        }

        // Pager con los pasos
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> NicknameStep(
                    nickname = uiState.nickname,
                    onNicknameChange = { viewModel.onNicknameChange(it) }
                )
                1 -> BirthYearStep(
                    birthYear = uiState.birthYear,
                    onBirthYearChange = { viewModel.onBirthYearChange(it) }
                )
                2 -> LastPeriodStep(
                    selectedDate = uiState.lastPeriodDate,
                    onDateSelected = { viewModel.onLastPeriodDateChange(it) }
                )
                3 -> ContraceptiveStep(
                    selectedType = uiState.contraceptiveType,
                    onTypeSelected = { viewModel.onContraceptiveTypeChange(it) }
                )
                4 -> CareMethodStep(
                    selectedMethod = uiState.careMethod,
                    onMethodSelected = { viewModel.onCareMethodChange(it) }
                )
            }
        }

        // Botones de navegación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botón Omitir/Atrás
            if (pagerState.currentPage > 0) {
                MoonlyOutlinedButton(
                    text = "Atrás",
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
                MoonlyOutlinedButton(
                    text = "Omitir",
                    onClick = { viewModel.skipOnboarding() },
                    modifier = Modifier.weight(1f)
                )
            }

            // Botón Siguiente/Finalizar
            MoonlyButton(
                text = if (pagerState.currentPage == 4) "Finalizar" else "Siguiente",
                onClick = {
                    if (pagerState.currentPage == 4) {
                        viewModel.completeOnboarding()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = !uiState.isLoading
            )
        }
    }
}