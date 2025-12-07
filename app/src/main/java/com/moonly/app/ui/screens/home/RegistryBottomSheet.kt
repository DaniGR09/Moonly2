package com.moonly.app.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moonly.app.domain.model.*
import com.moonly.app.ui.screens.home.BottomSheetState
import com.moonly.app.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Panel de registro deslizable (Bottom Sheet)
 */
@Composable
fun RegistryBottomSheet(
    selectedDate: LocalDate?,
    currentSymptoms: DailySymptoms?,
    bottomSheetState: BottomSheetState,
    onToggleSheet: () -> Unit,
    onClose: () -> Unit,
    onEmotionSelected: (Emotion) -> Unit,
    onBleedingAmountSelected: (BleedingAmount?) -> Unit,
    onBleedingColorSelected: (BleedingColor?) -> Unit,
    onFlowColorSelected: (FlowColor?) -> Unit,
    onCravingsChanged: (String?) -> Unit,
    onPainLevelSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (bottomSheetState == BottomSheetState.EXPANDED) {
                    Modifier.fillMaxHeight(0.88f)
                } else {
                    Modifier.height(240.dp)
                }
            ),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Handle (barra para arrastrar) + Botón cerrar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleSheet() }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Handle centrado
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Gray300)
                    )
                }

                // Botón X (solo visible cuando hay fecha seleccionada)
                if (selectedDate != null) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text(
                            text = "✕",
                            fontSize = 20.sp,
                            color = Gray600
                        )
                    }
                }
            }

            Divider(color = Gray200, thickness = 1.dp)

            if (selectedDate == null) {
                DisabledState()
            } else {
                if (bottomSheetState == BottomSheetState.MINIMIZED) {
                    MinimizedContent(
                        selectedDate = selectedDate,
                        currentSymptoms = currentSymptoms,
                        onExpand = onToggleSheet
                    )
                } else {
                    ExpandedContent(
                        selectedDate = selectedDate,
                        currentSymptoms = currentSymptoms,
                        onEmotionSelected = onEmotionSelected,
                        onBleedingAmountSelected = onBleedingAmountSelected,
                        onBleedingColorSelected = onBleedingColorSelected,
                        onFlowColorSelected = onFlowColorSelected,
                        onCravingsChanged = onCravingsChanged,
                        onPainLevelSelected = onPainLevelSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun DisabledState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Selecciona un día para ver o añadir tu registro",
            fontSize = 16.sp,
            color = Gray500,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MinimizedContent(
    selectedDate: LocalDate,
    currentSymptoms: DailySymptoms?,
    onExpand: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpand() }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = selectedDate.format(DateTimeFormatter.ofPattern("d 'de' MMMM yyyy", Locale("es"))),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MoonlyPurpleDark
        )

        CategoryRowMinimized(emoji = "😊", label = "Emociones", hasData = !currentSymptoms?.emotions.isNullOrEmpty())
        CategoryRowMinimized(emoji = "🩸", label = "Sangrado", hasData = currentSymptoms?.bleedingAmount != null)
        CategoryRowMinimized(emoji = "🎨", label = "Color", hasData = currentSymptoms?.bleedingColor != null)
        CategoryRowMinimized(emoji = "💧", label = "Flujo", hasData = currentSymptoms?.flowColor != null)
        CategoryRowMinimized(emoji = "🍕", label = "Antojos", hasData = currentSymptoms?.cravings != null)
        CategoryRowMinimized(emoji = "💊", label = "Dolor", hasData = currentSymptoms?.painLevel != null)
    }
}

@Composable
private fun CategoryRowMinimized(
    emoji: String,
    label: String,
    hasData: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 18.sp)
            Text(
                text = label,
                fontSize = 14.sp,
                color = if (hasData) MoonlyPurpleDark else Gray500
            )
        }

        if (hasData) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(MoonlyPurpleMedium)
            )
        }
    }
}

@Composable
private fun ExpandedContent(
    selectedDate: LocalDate,
    currentSymptoms: DailySymptoms?,
    onEmotionSelected: (Emotion) -> Unit,
    onBleedingAmountSelected: (BleedingAmount?) -> Unit,
    onBleedingColorSelected: (BleedingColor?) -> Unit,
    onFlowColorSelected: (FlowColor?) -> Unit,
    onCravingsChanged: (String?) -> Unit,
    onPainLevelSelected: (Int?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = selectedDate.format(DateTimeFormatter.ofPattern("d 'de' MMMM yyyy", Locale("es"))),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MoonlyPurpleDark
        )

        // 😊 Emociones
        CategorySection(
            title = "😊 Emociones",
            content = {
                EmotionSelector(
                    selectedEmotions = currentSymptoms?.emotions ?: emptyList(),
                    onEmotionSelected = onEmotionSelected
                )
            }
        )

        // 🩸 Sangrado
        CategorySection(
            title = "🩸 Flujo menstrual",
            content = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Cantidad", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Gray700)
                        BleedingAmountSelector(
                            selectedAmount = currentSymptoms?.bleedingAmount,
                            onAmountSelected = onBleedingAmountSelected
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Color", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Gray700)
                        BleedingColorSelector(
                            selectedColor = currentSymptoms?.bleedingColor,
                            onColorSelected = onBleedingColorSelected
                        )
                    }
                }
            }
        )

        // 💧 Flujo vaginal
        CategorySection(
            title = "💧 Flujo vaginal",
            content = {
                FlowColorSelector(
                    selectedColor = currentSymptoms?.flowColor,
                    onColorSelected = onFlowColorSelected
                )
            }
        )

        // 🍕 Antojos
        CategorySection(
            title = "🍕 Antojos",
            content = {
                Text(
                    text = "Próximamente...",
                    fontSize = 13.sp,
                    color = Gray500,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        )

        // 💊 Dolor
        CategorySection(
            title = "💊 Dolor",
            content = {
                PainLevelSelector(
                    selectedLevel = currentSymptoms?.painLevel,
                    onLevelSelected = onPainLevelSelected
                )
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun CategorySection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MoonlyPurpleDark
        )
        content()
    }
}

@Composable
private fun EmotionSelector(
    selectedEmotions: List<Emotion>,
    onEmotionSelected: (Emotion) -> Unit
) {
    val emotions = listOf(
        Emotion.FELIZ to "Feliz",
        Emotion.TRISTE to "Triste",
        Emotion.ESTRESADO to "Estresado",
        Emotion.ANSIOSO to "Ansioso",
        Emotion.ABURRIDO to "Aburrido",
        Emotion.EMOCIONADO to "Emocionado",
        Emotion.MOTIVADO to "Motivado",
        Emotion.CANSADO to "Cansado",
        Emotion.RELAJADO to "Relajado",
        Emotion.NOSTALGICO to "Nostálgico",
        Emotion.FRUSTRADO to "Frustrado",
        Emotion.APATICO to "Apático"
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(emotions) { (emotion, label) ->
            EmojiOptionWithLabel(
                emoji = "😊", // TODO: Cambiar por R.drawable.emotion_${emotion.name.lowercase()}
                label = label,
                isSelected = selectedEmotions.contains(emotion),
                onClick = { onEmotionSelected(emotion) }
            )
        }
    }
}

@Composable
private fun BleedingAmountSelector(
    selectedAmount: BleedingAmount?,
    onAmountSelected: (BleedingAmount?) -> Unit
) {
    val amounts = listOf(
        BleedingAmount.LIGERO to "Ligero",
        BleedingAmount.MODERADO to "Moderado",
        BleedingAmount.ABUNDANTE to "Abundante",
        BleedingAmount.MUY_ABUNDANTE to "Muy abundante"
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(amounts) { (amount, label) ->
            EmojiOptionWithLabel(
                emoji = "💧",
                label = label,
                isSelected = selectedAmount == amount,
                onClick = { onAmountSelected(if (selectedAmount == amount) null else amount) }
            )
        }
    }
}

@Composable
private fun BleedingColorSelector(
    selectedColor: BleedingColor?,
    onColorSelected: (BleedingColor?) -> Unit
) {
    val colors = listOf(
        BleedingColor.ROJO_BRILLANTE to "Rojo brillante",
        BleedingColor.ROJO_OSCURO to "Rojo oscuro",
        BleedingColor.MARRON to "Marrón",
        BleedingColor.NEGRO to "Negro"
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(colors) { (color, label) ->
            EmojiOptionWithLabel(
                emoji = "🔴",
                label = label,
                isSelected = selectedColor == color,
                onClick = { onColorSelected(if (selectedColor == color) null else color) }
            )
        }
    }
}

@Composable
private fun FlowColorSelector(
    selectedColor: FlowColor?,
    onColorSelected: (FlowColor?) -> Unit
) {
    val colors = listOf(
        FlowColor.TRANSPARENTE to "Transparente",
        FlowColor.BLANCO to "Blanco",
        FlowColor.AMARILLO to "Amarillo",
        FlowColor.MARRON to "Marrón"
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(colors) { (color, label) ->
            EmojiOptionWithLabel(
                emoji = "💦",
                label = label,
                isSelected = selectedColor == color,
                onClick = { onColorSelected(if (selectedColor == color) null else color) }
            )
        }
    }
}

@Composable
private fun PainLevelSelector(
    selectedLevel: Int?,
    onLevelSelected: (Int?) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items((1..10).toList()) { level ->
                PainLevelOption(
                    level = level,
                    isSelected = selectedLevel == level,
                    onClick = { onLevelSelected(if (selectedLevel == level) null else level) }
                )
            }
        }

        // Leyenda
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Sin dolor", fontSize = 11.sp, color = Gray500)
            Text(text = "Dolor intenso", fontSize = 11.sp, color = Gray500)
        }
    }
}

@Composable
private fun PainLevelOption(
    level: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MoonlyPurpleMedium else Gray300,
                shape = CircleShape
            )
            .background(if (isSelected) MoonlyPurpleMedium else White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = level.toString(),
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) White else MoonlyPurpleDark
        )
    }
}

@Composable
private fun EmojiOptionWithLabel(
    emoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(if (isSelected) MoonlyPurpleMedium else MoonlyPinkLight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 26.sp
            )
        }

        Text(
            text = label,
            fontSize = 11.sp,
            color = if (isSelected) MoonlyPurpleDark else Gray600,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 70.dp)
        )
    }
}