package com.moonly.app.ui.screens.profile.contraceptives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moonly.app.domain.model.Contraceptive
import com.moonly.app.domain.model.ContraceptiveType
import com.moonly.app.ui.theme.*

@Composable
fun ContraceptivesScreen(
    onNavigateBack: () -> Unit,
    viewModel: ContraceptivesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedContraceptive by remember { mutableStateOf<Contraceptive?>(null) }

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
                        text = "Anticonceptivos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoonlyPurpleDark
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MoonlyPurpleMedium
            ) {
                Text(text = "+", fontSize = 24.sp, color = White)
            }
        },
        containerColor = Gray50
    ) { paddingValues ->
        if (uiState.contraceptives.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay anticonceptivos registrados",
                    fontSize = 16.sp,
                    color = Gray500
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.contraceptives) { contraceptive ->
                    ContraceptiveCard(
                        contraceptive = contraceptive,
                        onClick = { selectedContraceptive = contraceptive },
                        onDelete = { viewModel.deleteContraceptive(contraceptive.id) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddContraceptiveDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { type, name, notificationEnabled ->
                viewModel.addContraceptive(type, name, notificationEnabled)
                showAddDialog = false
            }
        )
    }

    selectedContraceptive?.let { contraceptive ->
        EditContraceptiveDialog(
            contraceptive = contraceptive,
            onDismiss = { selectedContraceptive = null },
            onConfirm = { updatedType, updatedName, updatedNotification ->
                viewModel.updateContraceptive(
                    contraceptive.id,
                    updatedType,
                    updatedName,
                    updatedNotification
                )
                selectedContraceptive = null
            }
        )
    }
}

@Composable
private fun ContraceptiveCard(
    contraceptive: Contraceptive,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contraceptive.contraceptiveType.getDisplayName(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MoonlyPurpleDark
                )

                if (contraceptive.contraceptiveName != null) {
                    Text(
                        text = contraceptive.contraceptiveName,
                        fontSize = 14.sp,
                        color = Gray600
                    )
                }

                if (contraceptive.notificationEnabled) {
                    Text(
                        text = "🔔 Notificaciones activadas",
                        fontSize = 12.sp,
                        color = MoonlyPurpleMedium
                    )
                }
            }

            IconButton(onClick = { showDeleteDialog = true }) {
                Text(text = "🗑️", fontSize = 20.sp)
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar anticonceptivo?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteDialog = false
                }) {
                    Text("Eliminar", color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddContraceptiveDialog(
    onDismiss: () -> Unit,
    onConfirm: (ContraceptiveType, String?, Boolean) -> Unit
) {
    var selectedType by remember { mutableStateOf<ContraceptiveType?>(null) }
    var name by remember { mutableStateOf("") }
    var notificationEnabled by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar anticonceptivo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Dropdown de tipo
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedType?.getDisplayName() ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ContraceptiveType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.getDisplayName()) },
                                onClick = {
                                    selectedType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Activar notificaciones")
                    Switch(
                        checked = notificationEnabled,
                        onCheckedChange = { notificationEnabled = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedType?.let { onConfirm(it, name.ifBlank { null }, notificationEnabled) }
                },
                enabled = selectedType != null
            ) {
                Text("Agregar", color = MoonlyPurpleMedium)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditContraceptiveDialog(
    contraceptive: Contraceptive,
    onDismiss: () -> Unit,
    onConfirm: (ContraceptiveType, String?, Boolean) -> Unit
) {
    var selectedType by remember { mutableStateOf(contraceptive.contraceptiveType) }
    var name by remember { mutableStateOf(contraceptive.contraceptiveName ?: "") }
    var notificationEnabled by remember { mutableStateOf(contraceptive.notificationEnabled) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar anticonceptivo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedType.getDisplayName(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ContraceptiveType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.getDisplayName()) },
                                onClick = {
                                    selectedType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Activar notificaciones")
                    Switch(
                        checked = notificationEnabled,
                        onCheckedChange = { notificationEnabled = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedType, name.ifBlank { null }, notificationEnabled) }
            ) {
                Text("Guardar", color = MoonlyPurpleMedium)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}