package com.example.notificationanimateapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        val notificationManager=applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH).apply {
            enableVibration(false) // Disable vibration
        }
        notificationManager.createNotificationChannel(notificationChannel)
    }

}