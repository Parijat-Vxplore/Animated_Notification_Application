package com.example.notificationanimateapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    private var currentStep = 0
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val notificationService = NotificationService(applicationContext)
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    showNotification(notificationService)
                }) {
                    Text("Show notification")
                }
            }
        }
    }

    private fun showNotification(notificationService: NotificationService) {
        val totalSteps = 205
        val moveByPixel = 50
        handler.post(object : Runnable {
            override fun run() {
                val foregroundNonExpandedBitmap = applicationContext.bitmapFromResource(R.drawable.car)
                val backgroundNonExpandedBitmap = multiplyBitmap(4,applicationContext.bitmapFromResource(R.drawable.road))
                val combinedNonExpandedBitmap = margeBitmaps(backgroundNonExpandedBitmap!!, foregroundNonExpandedBitmap,(currentStep * moveByPixel).toFloat(), 700f)


                val foregroundExpandedBitmap = applicationContext.bitmapFromResource(R.drawable.car)
                val backgroundExpandedBitmap = applicationContext.bitmapFromResource(R.drawable.road)
                val combinedExpandedBitmap = margeBitmaps(backgroundExpandedBitmap, foregroundExpandedBitmap,(currentStep * moveByPixel).toFloat(), 700f)


                notificationService.updateNotification(combinedNonExpandedBitmap,combinedExpandedBitmap,currentStep < totalSteps - 1)
                currentStep = (currentStep + 1)
                if (currentStep < totalSteps)
                    handler.postDelayed(this, 150) // Update every second (150ms)
                else
                    currentStep = 0
            }

            private fun multiplyBitmap(count: Int, bitmapFromResource: Bitmap?): Bitmap? {
                var result = bitmapFromResource
                var i=0
                while (i < count-1) {
                    result=addBitmaps(result!!, bitmapFromResource!!)
                    i++
                }
                return result
            }
        })
    }

    // Function to combine two bitmaps side by side
    fun addBitmaps(bitmap1: Bitmap, bitmap2: Bitmap): Bitmap {
        val combinedWidth = bitmap1.width + bitmap2.width
        val maxHeight = Math.max(bitmap1.height, bitmap2.height)
        val combinedBitmap = Bitmap.createBitmap(combinedWidth, maxHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(combinedBitmap)
        canvas.drawBitmap(bitmap1, 0f, 0f, Paint())
        canvas.drawBitmap(bitmap2, bitmap1.width.toFloat(), 0f, Paint())
        return combinedBitmap
    }


    // Function to combine two bitmaps as background and foreground
    fun margeBitmaps(
        backgroundBitmap: Bitmap,
        foregroundBitmap: Bitmap,
        foregroundX: Float,
        foregroundY: Float
    ): Bitmap {
        val combinedBitmap = Bitmap.createBitmap(
            backgroundBitmap.width,
            backgroundBitmap.height,
            backgroundBitmap.config
        )
        val canvas = Canvas(combinedBitmap)
        canvas.drawBitmap(backgroundBitmap, 0f, 0f, null)
        canvas.drawBitmap(foregroundBitmap, foregroundX, foregroundY, Paint())
        return combinedBitmap
    }


    private fun Context.bitmapFromResource(@DrawableRes resId: Int) =
        BitmapFactory.decodeResource(resources, resId)
}