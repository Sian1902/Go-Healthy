package com.example.gohealthy.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.gohealthy.notification.NotificationService

class AlarmReciever:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        Log.d("AlarmReciever", "Alarm triggered: $message")
        val notificationService = NotificationService(context!!)
        notificationService.showNotification()
    }
}