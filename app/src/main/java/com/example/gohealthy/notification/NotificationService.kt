package com.example.gohealthy.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.gohealthy.R
import com.example.gohealthy.view.MainActivity

class NotificationService(private val context: Context) {
    private val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    fun showNotification(){
        val activityIntent= Intent(context, MainActivity::class.java)
        val activityPendingIntent= PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE)
        val notification=NotificationCompat.Builder(context, DAILY_REPORTS_CHANNEL_ID)
            .setSmallIcon(R.drawable.go_healthy_icon)
            .setContentTitle("Daily report is ready")
            .setContentText("Nice job today see your stats")
            .setContentIntent(activityPendingIntent)
            .build()
        notificationManager.notify(1,notification)

    }
    companion object{
        const val DAILY_REPORTS_CHANNEL_ID = "daily_reports_channel"
    }
}