package dev.easysouls.tracetrail.domain.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dev.easysouls.tracetrail.R

// While the service is active, all of the application is active
// So I don't have to put all the code I need here, I can use other classes

class NavigationService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "navigation_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Navigation is active")
            .setContentInfo("Content info")
            .build()
        startForeground(1, notification)
    }

    enum class Actions {
        START, STOP
    }
}