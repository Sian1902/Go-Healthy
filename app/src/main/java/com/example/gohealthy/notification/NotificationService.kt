package com.example.gohealthy.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.gohealthy.R
import com.example.gohealthy.alarm.AlarmItem
import com.example.gohealthy.alarm.AndroidAlarmScheduler
import com.example.gohealthy.helpers.DailyData
import com.example.gohealthy.helpers.PrefManager
import com.example.gohealthy.history.HistoryItem
import com.example.gohealthy.view.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.time.LocalDateTime

class NotificationService(private val context: Context) {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db = Firebase.firestore
    private val currentUser = auth.currentUser
    val prefManager: PrefManager =PrefManager(context)
    private val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    fun showNotification(){
        DailyData.oldHistoryItem.steps=prefManager.loadSteps().toInt()
        DailyData.oldHistoryItem.kcalIn=prefManager.loadCaloriesIn()
        DailyData.oldHistoryItem.kcalOut=prefManager.loadCaloriesOut()
        if(currentUser!=null){
            db.collection("users").document(currentUser.uid).collection("history").add(
               mapOf( "date" to LocalDateTime.now().toLocalDate().toString(),
                "steps" to prefManager.loadSteps().toInt(),
                "kcalIn" to prefManager.loadCaloriesIn(),
                "kcalOut" to prefManager.loadCaloriesOut())
            ).addOnSuccessListener {
                Log.d("success","Fetch is successful")
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
            prefManager.saveCaloriesIn(0)
            prefManager.saveCaloriesOut(0)
            prefManager.resetSteps(0f)

        }
        val activityIntent = Intent(context, MainActivity::class.java)
        activityIntent.putExtra("openFragment", "DailyReport")
        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, DAILY_REPORTS_CHANNEL_ID)
            .setSmallIcon(R.drawable.go_healthy_icon)
            .setContentTitle("Daily report is ready")
            .setContentText("Nice job today, see your stats")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(1, notification)
        val date = LocalDateTime.now().plusDays(1)
        val alarmItem= AlarmItem(date.withHour(23).withMinute(59),"test")
        val scheduler= AndroidAlarmScheduler(context)
        alarmItem.let(scheduler::schedule)
    }
    companion object{
        const val DAILY_REPORTS_CHANNEL_ID = "daily_reports_channel"
    }
}