package com.moonly.app.ui.screens.profile.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.moonly.app.ui.components.buttons.MoonlyButton
import com.moonly.app.ui.components.textfields.MoonlyTextField
import com.moonly.app.ui.theme.*

/**
 * Pantalla de ajustes de perfil
 */
@Composable
fun ProfileSettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var showEditNicknameDialog by remember { mutableStateOf(false) }
    var showEditBirthYearDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    // Manejar logout exitoso
    LaunchedEffect(uiState.logoutSuccess) {
        if (uiState.logoutSuccess) {
            onLogout()
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Text(text = "←", fontSize = 24.sp, color = MoonlyPurpleMedium)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Ajustes de perfil",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoonlyPurpleDark
                    )
                }
            }
        },
        containerColor = Gray50
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Información personal
            SectionTitle(title = "Información personal")

            SettingsItem(
                title = "Apodo",
                value = uiState.nickname ?: "Sin apodo",
                onClick = { showEditNicknameDialog = true }
            )

            SettingsItem(
                title = "Año de nacimiento",
                value = uiState.birthYear?.toString() ?: "No especificado",
                onClick = { showEditBirthYearDialog = true }
            )

            SettingsItem(
                title = "Correo electrónico",
                value = uiState.email ?: "",
                onClick = null // No editable por ahora
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Seguridad
            SectionTitle(title = "Seguridad")

            SettingsItem(
                title = "Cambiar contraseña",
                icon = "🔒",
                onClick = { showChangePasswordDialog = true }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Cuenta
            SectionTitle(title = "Cuenta")

            DangerButton(
                text = "Eliminar cuenta",
                onClick = { showDeleteAccountDialog = true }
            )

            MoonlyButton(
                text = "Cerrar sesión",
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Diálogos
    if (showEditNicknameDialog) {
        EditNicknameDialog(
            currentNickname = uiState.nickname,
            onDismiss = { showEditNicknameDialog = false },
            onConfirm = { newNickname ->
                viewModel.updateNickname(newNickname)
                showEditNicknameDialog = false
            }
        )
    }

    if (showEditBirthYearDialog) {
        EditBirthYearDialog(
            currentYear = uiState.birthYear,
            onDismiss = { showEditBirthYearDialog = false },
            onConfirm = { newYear ->
                viewModel.updateBirthYear(newYear)
                showEditBirthYearDialog = false
            }
        )
    }

    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showChangePasswordDialog = false },
            onConfirm = { currentPassword, newPassword ->
                viewModel.changePassword(currentPassword, newPassword)
                showChangePasswordDialog = false
            }
        )
    }

    if (showDeleteAccountDialog) {
        DeleteAccountDialog(
            onDismiss = { showDeleteAccountDialog = false },
            onConfirm = {
                viewModel.deleteAccount()
                showDeleteAccountDialog = false
            }
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Gray600,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    title: String,
    value: String? = null,
    icon: String? = null,
    onClick: (() -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
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
                if (icon != null) {
                    Text(text = icon, fontSize = 20.sp)
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MoonlyPurpleDark
                    )

                    if (value != null) {
                        Text(
                            text = value,
                            fontSize = 13.sp,
                            color = Gray500
                        )
                    }
                }
            }

            if (onClick != null) {
                Text(text = "→", fontSize = 20.sp, color = Gray400)
            }
        }
    }
}

@Composable
private fun DangerButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ErrorRed,
            contentColor = White
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

// ==================== DIÁLOGOS ====================

@Composable
private fun EditNicknameDialog(
    currentNickname: String?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var nickname by remember { mutableStateOf(currentNickname ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar apodo") },
        text = {
            MoonlyTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = "Apodo",
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(nickname) }) {
                Text("Guardar", color = MoonlyPurpleMedium)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Gray600)
            }
        }
    )
}

@Composable
private fun EditBirthYearDialog(
    currentYear: Int?,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var year by remember { mutableStateOf(currentYear?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar año de nacimiento") },
        text = {
            MoonlyTextField(
                value = year,
                onValueChange = { if (it.length <= 4 && it.all { char -> char.isDigit() }) year = it },
                label = "Año",
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    year.toIntOrNull()?.let { onConfirm(it) }
                },
                enabled = year.length == 4
            ) {
                Text("Guardar", color = MoonlyPurpleMedium)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Gray600)
            }
        }
    )
}

@Composable
private fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cambiar contraseña") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MoonlyTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = "Contraseña actual",
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                MoonlyTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "Nueva contraseña",
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                MoonlyTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirmar contraseña",
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty() && newPassword != confirmPassword) {
                    Text(
                        text = "Las contraseñas no coinciden",
                        fontSize = 12.sp,
                        color = ErrorRed
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(currentPassword, newPassword) },
                enabled = currentPassword.isNotEmpty() &&
                        newPassword.length >= 8 &&
                        newPassword == confirmPassword
            ) {
                Text("Cambiar", color = MoonlyPurpleMedium)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Gray600)
            }
        }
    )
}

@Composable
private fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("¿Eliminar cuenta?") },
        text = {
            Text(
                "Esta acción es permanente y no se puede deshacer. Se eliminarán todos tus datos.",
                fontSize = 14.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Eliminar", color = ErrorRed, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Gray600)
            }
        }
    )
}