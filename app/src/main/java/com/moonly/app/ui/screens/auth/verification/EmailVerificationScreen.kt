package com.moonly.app.ui.screens.auth.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moonly.app.ui.components.buttons.MoonlyButton
import com.moonly.app.ui.components.buttons.MoonlyOutlinedButton
import com.moonly.app.ui.components.dialogs.LoadingDialog

@Composable
fun EmailVerificationScreen(
    viewModel: EmailVerificationViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        LoadingDialog(message = "Reenviando correo...")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono de email en círculo
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(
                    color = Color(0xFFFFE1FF),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MarkEmailRead,
                contentDescription = "Email enviado",
                tint = Color(0xFF7E60BF),
                modifier = Modifier.size(70.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Título
        Text(
            text = "Revisa tu correo",
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF433878),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        Text(
            text = "Te hemos enviado un enlace de verificación. Revisa tu bandeja de entrada y tu carpeta de spam.",
            fontSize = 16.sp,
            color = Color(0xFF9E9E9E),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Mensaje de estado
        if (uiState.message != null) {
            Text(
                text = uiState.message!!,
                color = if (uiState.isError) Color(0xFFD32F2F) else Color(0xFF388E3C),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

        // Botón continuar sin verificar
        MoonlyButton(
            text = "Continuar sin verificar",
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón reenviar correo
        MoonlyOutlinedButton(
            text = "Reenviar correo",
            onClick = { viewModel.resendVerificationEmail() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Link a login
        MoonlyOutlinedButton(
            text = "Volver al inicio de sesión",
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}