package com.moonly.app.ui.screens.auth.register

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
import com.moonly.app.ui.components.buttons.MoonlyTextButton
import com.moonly.app.ui.components.dialogs.LoadingDialog
import com.moonly.app.ui.components.textfields.MoonlyPasswordField
import com.moonly.app.ui.components.textfields.MoonlyTextField

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToEmailVerification: () -> Unit // ⚠️ NUEVO parámetro
) {
    val uiState by viewModel.uiState.collectAsState()

    // ⚠️ LÓGICA CORREGIDA: Navegar según verificación
    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            if (uiState.isEmailVerified) {
                // Email ya verificado → ir a onboarding
                onNavigateToOnboarding()
            } else {
                // Email NO verificado → ir a pantalla de verificación
                onNavigateToEmailVerification()
            }
        }
    }

    if (uiState.isLoading) {
        LoadingDialog(message = "Creando tu cuenta...")
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
            text = "Crear tu cuenta",
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF433878),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtítulo
        Text(
            text = "Comienza tu camino hacia el bienestar",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF9E9E9E),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Campo de correo
        MoonlyTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = "Correo electrónico",
            placeholder = "tu@correo.com",
            isError = uiState.emailError != null,
            errorMessage = uiState.emailError,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        MoonlyPasswordField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = "Contraseña",
            placeholder = "Mínimo 8 caracteres",
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirmar contraseña
        MoonlyPasswordField(
            value = uiState.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChange(it) },
            label = "Confirmar contraseña",
            placeholder = "Repite tu contraseña",
            isError = uiState.confirmPasswordError != null,
            errorMessage = uiState.confirmPasswordError,
            imeAction = ImeAction.Done,
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                onDone = { viewModel.onRegisterClick() }
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

        Spacer(modifier = Modifier.height(28.dp))

        // Botón de registro
        MoonlyButton(
            text = "Crear cuenta",
            onClick = { viewModel.onRegisterClick() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(20.dp))

        // Ya tengo cuenta
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = "¿Ya tienes cuenta?",
                color = Color(0xFF757575),
                fontSize = 15.sp
            )
            MoonlyTextButton(
                text = "Inicia sesión",
                onClick = onNavigateToLogin
            )
        }
    }
}