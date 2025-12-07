package com.moonly.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moonly.app.ui.theme.*

/**
 * Menú principal de Perfil
 */
@Composable
fun ProfileMenuScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToContraceptives: () -> Unit,
    onNavigateToMedicalConditions: () -> Unit,
    onNavigateToCareMethods: () -> Unit,
    viewModel: ProfileMenuViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ProfileTopBar(
                nickname = uiState.user?.nickname,
                email = uiState.user?.email,
                onNavigateBack = onNavigateBack
            )
        },
        containerColor = Gray50
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Ajustes de perfil",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Gray600,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )

            // ⚙️ Ajustes de perfil
            ProfileMenuItem(
                icon = "⚙️",
                title = "Perfil",
                onClick = onNavigateToSettings
            )

            // 💊 Anticonceptivos
            ProfileMenuItem(
                icon = "💊",
                title = "Anticonceptivos",
                subtitle = if (uiState.activeContraceptives > 0)
                    "${uiState.activeContraceptives} activo${if (uiState.activeContraceptives > 1) "s" else ""}"
                else null,
                onClick = onNavigateToContraceptives
            )

            // 🏥 Condiciones Médicas
            ProfileMenuItem(
                icon = "🏥",
                title = "Condiciones médicas",
                subtitle = if (uiState.activeMedicalConditions > 0)
                    "${uiState.activeMedicalConditions} registrada${if (uiState.activeMedicalConditions > 1) "s" else ""}"
                else null,
                onClick = onNavigateToMedicalConditions
            )

            // 🧼 Método de Cuidado
            ProfileMenuItem(
                icon = "🧼",
                title = "Método de cuidado menstrual",
                subtitle = uiState.currentCareMethod,
                onClick = onNavigateToCareMethods
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * TopBar con info del usuario
 */
@Composable
private fun ProfileTopBar(
    nickname: String?,
    email: String?,
    onNavigateBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = White,
        shadowElevation = 2.dp
    ) {
        Column {
            // Header con botón atrás
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Text(text = "←", fontSize = 24.sp, color = MoonlyPurpleMedium)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Perfil",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoonlyPurpleDark
                )
            }

            // Info del usuario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MoonlyPinkLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nickname?.firstOrNull()?.uppercase() ?: "👤",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoonlyPurpleMedium
                    )
                }

                // Nombre y correo
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = nickname ?: "Usuario",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MoonlyPurpleDark
                    )

                    if (email != null) {
                        Text(
                            text = email,
                            fontSize = 14.sp,
                            color = Gray600
                        )
                    }
                }
            }
        }
    }
}

/**
 * Item de menú
 */
@Composable
private fun ProfileMenuItem(
    icon: String,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = icon, fontSize = 24.sp)

                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MoonlyPurpleDark
                    )

                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            fontSize = 13.sp,
                            color = Gray500
                        )
                    }
                }
            }

            Text(
                text = "→",
                fontSize = 20.sp,
                color = Gray400
            )
        }
    }
}