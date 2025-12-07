package com.moonly.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = MoonlyPurpleMedium,
    onPrimary = White,
    primaryContainer = MoonlyPinkLight,
    onPrimaryContainer = MoonlyPurpleDark,

    secondary = MoonlyOvulationTeal,
    onSecondary = White,
    secondaryContainer = MoonlyPinkLight,
    onSecondaryContainer = MoonlyPurpleDark,

    tertiary = MoonlyPurpleDark,
    onTertiary = White,

    background = White, // ✅ Fondo blanco
    onBackground = MoonlyPurpleDark,

    surface = White,
    onSurface = MoonlyPurpleDark,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray700,

    error = ErrorRed,
    onError = White,

    outline = Gray300,
    outlineVariant = Gray200
)

private val DarkColorScheme = darkColorScheme(
    primary = MoonlyPurpleMedium,
    onPrimary = White,
    primaryContainer = MoonlyPurpleDark,
    onPrimaryContainer = MoonlyPinkLight,

    secondary = MoonlyOvulationTeal,
    onSecondary = Black,
    secondaryContainer = MoonlyPurpleDark,
    onSecondaryContainer = MoonlyPinkLight,

    tertiary = MoonlyPinkLight,
    onTertiary = MoonlyPurpleDark,

    background = Gray900,
    onBackground = White,

    surface = Gray800,
    onSurface = White,
    surfaceVariant = Gray700,
    onSurfaceVariant = Gray300,

    error = ErrorRed,
    onError = White,

    outline = Gray600,
    outlineVariant = Gray700
)

@Composable
fun MoonlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // ✅ CAMBIO: Barra de estado blanca en vez de morada
            window.statusBarColor = Color.White.toArgb()
            // ✅ Iconos oscuros en la barra de estado (para que se vean en fondo blanco)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MoonlyTypography,
        shapes = MoonlyShapes,
        content = content
    )
}