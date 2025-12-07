package com.moonly.app.ui.screens.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
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
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToPasswordRecovery: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onNavigateToHome()
        }
    }

    if (uiState.isLoading) {
        LoadingDialog(message = "Iniciando sesión...")
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
            text = "¡Hola de nuevo!",
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF433878),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtítulo
        Text(
            text = "Nos alegra verte otra vez",
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
            placeholder = "Ingresa tu contraseña",
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError,
            imeAction = ImeAction.Done,
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                onDone = { viewModel.onLoginClick() }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Olvidé contraseña
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            MoonlyTextButton(
                text = "¿Olvidaste tu contraseña?",
                onClick = onNavigateToPasswordRecovery
            )
        }

        // Error general
        if (uiState.generalError != null) {
            Spacer(modifier = Modifier.height(12.dp))
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

        // Botón de login
        MoonlyButton(
            text = "Iniciar sesión",
            onClick = { viewModel.onLoginClick() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(20.dp))

        // Crear cuenta - más pegado al botón
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = "¿No tienes cuenta?",
                color = Color(0xFF757575),
                fontSize = 15.sp
            )
            MoonlyTextButton(
                text = "Crear una",
                onClick = onNavigateToRegister
            )
        }
    }
}