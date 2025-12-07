package com.moonly.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.moonly.app.data.local.datastore.PreferencesDataStore
import com.moonly.app.ui.navigation.AppNavigation
import com.moonly.app.ui.theme.MoonlyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesDataStore: PreferencesDataStore

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Debug: ver estados
        runBlocking {
            Log.d(TAG, "isLoggedIn: ${preferencesDataStore.getIsLoggedIn().first()}")
            Log.d(TAG, "hasOnboarding: ${preferencesDataStore.getHasCompletedOnboarding().first()}")
        }

        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            MoonlyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(navController = navController)
                }
            }
        }

        // ⚠️ NUEVO: Manejar deep links al iniciar
        handleDeepLink(intent)
    }

    // ⚠️ NUEVO: Manejar deep links cuando la app ya está abierta
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    /**
     * Maneja los deep links de Supabase
     */
    private fun handleDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data

        if (data != null) {
            Log.d(TAG, "🔗 Deep link recibido: $data")

            // Verificar si es un link de reset password
            if (data.path?.contains("reset-password") == true) {
                val token = data.getQueryParameter("token")
                Log.d(TAG, "🔑 Token de reset: $token")

                if (!token.isNullOrBlank()) {
                    // El NavController manejará la navegación automáticamente
                    // gracias al deep link configurado en AppNavigation
                    Log.d(TAG, "✅ Deep link válido para reset password")
                } else {
                    Log.e(TAG, "❌ Token inválido en deep link")
                }
            }

            // Verificar si es un link de confirmación de email
            else if (data.path?.contains("confirm") == true ||
                data.fragment?.contains("type=signup") == true) {
                Log.d(TAG, "✅ Link de confirmación de email detectado")
                // Supabase maneja esto automáticamente
            }
        }
    }
}