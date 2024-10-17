package com.example.gohealthy.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.layout.IntervalList
import com.example.gohealthy.history.HistoryItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.time.ZoneId

class AndroidAlarmScheduler(
    private val context: Context
):AlarmScheduler {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db = Firebase.firestore
    private val currentUser = auth.currentUser
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    override fun schedule(item: AlarmItem,historyItem: HistoryItem) {

       if(currentUser!=null){
           db.collection("users").document(currentUser.uid).collection("history").add(historyItem).addOnSuccessListener {
               Log.d("success","Fetch is successful")
           }.addOnFailureListener {
            Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
           }
       }





        val intent = Intent(context, AlarmReciever::class.java).apply {
            putExtra("EXTRA_MESSAGE",item.message)
        }
       /*alarmManager.setRepeating(
           AlarmManager.RTC_WAKEUP,
           item.time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000,
           AlarmManager.INTERVAL_FIFTEEN_MINUTES,
           PendingIntent.getBroadcast(
               context,
               item.hashCode(),
               intent,
               PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
           )
       )*/
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        )
    }

    override fun cancel(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}