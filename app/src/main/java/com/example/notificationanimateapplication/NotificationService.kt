package com.example.notificationanimateapplication

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.core.app.NotificationCompat

const val NOTIFICATION_CHANNEL_ID = "ch-1"
const val NOTIFICATION_CHANNEL_NAME = "Test Notification"
const val NOTIFICATION_ID = 100
const val REQUEST_CODE = 200

class NotificationService(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val myIntent = Intent(context,MainActivity::class.java)
    private val pendingIntent = PendingIntent.getActivity(
        context,
        REQUEST_CODE,
        myIntent,
        PendingIntent.FLAG_IMMUTABLE
    )


    fun updateNotification(nonExpandedBitmapImage: Bitmap,expandedBitmapImage: Bitmap, isOngoing: Boolean) {
        val customNonExpandedView = RemoteViews(context.packageName, R.layout.custom_notification).apply {
            setImageViewBitmap(R.id.imageView,nonExpandedBitmapImage)
        }
        val customExpandedView = RemoteViews(context.packageName, R.layout.custom_notification).apply {
            setImageViewBitmap(R.id.imageView,expandedBitmapImage)
        }

        val notification=NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.car)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(customNonExpandedView)
            .setCustomBigContentView(customExpandedView)
            .setCustomHeadsUpContentView(null)
            .setContentIntent(pendingIntent)
            .setOngoing(isOngoing)
            .build()
        notificationManager.notify(NOTIFICATION_ID,notification)
    }




}