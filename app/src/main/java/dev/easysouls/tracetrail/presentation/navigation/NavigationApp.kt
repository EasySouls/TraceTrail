package dev.easysouls.tracetrail.presentation.navigation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class NavigationApp: Application() {

    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            "navigation_channel",
            "Navigation Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}