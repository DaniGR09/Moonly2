package com.moonly.app.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moonly.app.R
import com.moonly.app.core.constants.AppConstants
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateTo: String?,
    onNavigationHandled: (String) -> Unit
) {
    var logoAlpha by remember { mutableStateOf(0f) }
    var textAlpha by remember { mutableStateOf(0f) }
    var hasNavigated by remember { mutableStateOf(false) }

    // Animaciones suaves
    LaunchedEffect(Unit) {
        delay(200)
        logoAlpha = 1f
        delay(400)
        textAlpha = 1f
    }

    // Navegación controlada
    LaunchedEffect(navigateTo) {
        if (navigateTo != null && !hasNavigated) {
            hasNavigated = true
            onNavigationHandled(navigateTo)
        }
    }

    val animatedLogoAlpha by animateFloatAsState(
        targetValue = logoAlpha,
        animationSpec = tween(800, easing = FastOutSlowInEasing)
    )

    val animatedTextAlpha by animateFloatAsState(
        targetValue = textAlpha,
        animationSpec = tween(600, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Logo con animación
            Image(
                painter = painterResource(id = R.drawable.ic_moonly_logo),
                contentDescription = "Logo de Moonly",
                modifier = Modifier
                    .size(140.dp)
                    .alpha(animatedLogoAlpha)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nombre de la app
            Text(
                text = AppConstants.APP_NAME,
                fontSize = 52.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF7E60BF),
                modifier = Modifier.alpha(animatedTextAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}