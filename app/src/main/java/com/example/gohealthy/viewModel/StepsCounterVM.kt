package com.example.gohealthy.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gohealthy.helpers.DailyData

class StepsCounterVM:ViewModel(){
    private var totalSteps=0f
    private var previousTotalSteps = 0f
    private var _currentSteps:MutableLiveData<Int> = MutableLiveData(0)
    val currentSteps:LiveData<Int> get()=_currentSteps


   fun updateSteps(steps:Int){
       totalSteps = steps.toFloat()

       _currentSteps.value =totalSteps.toInt()-previousTotalSteps.toInt()
       DailyData.newHistoryItem.steps=_currentSteps.value!!
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