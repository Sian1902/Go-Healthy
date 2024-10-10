package com.example.gohealthy.ViewModel

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StepsCounterVM:ViewModel(){
    private var totalSteps=0f
    private var previousTotalSteps = 0f
    private var _currentSteps:MutableLiveData<Int> = MutableLiveData(0)
    val currentSteps:LiveData<Int> get()=_currentSteps


   fun updateSteps(steps:Int){
       totalSteps = steps.toFloat()

       _currentSteps.value =totalSteps.toInt()-previousTotalSteps.toInt()
   }
     fun loadData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        previousTotalSteps = savedNumber
    }
     fun resetSteps(context: Context) {
        val sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.apply()
    }


}