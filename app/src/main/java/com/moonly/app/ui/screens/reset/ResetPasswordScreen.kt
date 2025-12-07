package com.moonly.app.ui.screens.auth.reset

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moonly.app.R
import com.moonly.app.ui.components.buttons.MoonlyButton
import com.moonly.app.ui.components.dialogs.LoadingDialog
import com.moonly.app.ui.components.textfields.MoonlyPasswordField

@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    token: String?,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Validar token al iniciar
    LaunchedEffect(token) {
        if (token.isNullOrBlank()) {
            viewModel.setError("Token inválido o expirado")
        } else {
            viewModel.setToken(token)
        }
    }

    // Navegar cuando sea exitoso
    LaunchedEffect(uiState.resetSuccess) {
        if (uiState.resetSuccess) {
            onNavigateToLogin()
        }
    }

    if (uiState.isLoading) {
        LoadingDialog(message = "Actualizando contraseña...")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.ic_moonly_logo),
            contentDescription = "Logo Moonly",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Título
        Text(
            text = "Nueva contraseña",
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF433878),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Descripción
        Text(
            text = "Ingresa tu nueva contraseña",
            fontSize = 16.sp,
            color = Color(0xFF9E9E9E),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Campo de nueva contraseña
        MoonlyPasswordField(
            value = uiState.newPassword,
            onValueChange = { viewModel.onNewPasswordChange(it) },
            label = "Nueva contraseña",
            placeholder = "Mínimo 8 caracteres",
            isError = uiState.newPasswordError != null,
            errorMessage = uiState.newPasswordError,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de confirmar contraseña
        MoonlyPasswordField(
            value = uiState.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChange(it) },
            label = "Confirmar contraseña",
            placeholder = "Repite tu contraseña",
            isError = uiState.confirmPasswordError != null,
            errorMessage = uiState.confirmPasswordError,
            imeAction = ImeAction.Done,
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                onDone = { viewModel.onResetClick() }
            )
        )

        // Error general
        if (uiState.generalError != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiState.generalError!!,
                color = Color(0xFFD32F2F),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de actualizar
        MoonlyButton(
            text = "Actualizar contraseña",
            onClick = { viewModel.onResetClick() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && uiState.token != null
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(32.dp))
    }
}