package com.moonly.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.moonly.app.core.constants.AppConstants
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase Application principal con Hilt
 */
@HiltAndroidApp
class MoonlyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Crear canal de notificaciones para FCM
        createNotificationChannel()
    }

    /**
     * Crea el canal de notificaciones para Android O+
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AppConstants.FCM_CHANNEL_ID,
                AppConstants.FCM_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = AppConstants.FCM_CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}