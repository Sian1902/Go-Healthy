package com.example.gohealthy.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gohealthy.helpers.DailyData
import com.example.gohealthy.nutritionixAPI.CallDecider
import com.example.gohealthy.nutritionixAPI.NutritionixQuery
import com.example.gohealthy.nutritionixAPI.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NutritionixVM:ViewModel() {
    private var _breakfastCalories= MutableLiveData(0.0)
    private var _lunchCalories= MutableLiveData(0.0)
    private var _dinnerCalories= MutableLiveData(0.0)
    private var _workoutCalories= MutableLiveData(0)

    val breakFastCalories:MutableLiveData<Double> get() = _breakfastCalories
    val lunchCalories:MutableLiveData<Double> get() = _lunchCalories
    val dinnerCalories:MutableLiveData<Double> get() = _dinnerCalories
    val workoutCalories:MutableLiveData<Int> get() = _workoutCalories
     fun foodCall(food: String, callDecider: CallDecider) =viewModelScope.launch(Dispatchers.IO) {
            Log.d("response", "function called")
            val query = NutritionixQuery(food)

            val response = RetrofitClient.instance.getNutritionData(query)
            var calories = 0.0

            if (response.isSuccessful && response.body() != null) {
                response.body()!!.foods.let { foods ->
                    foods.forEach {
                        calories += it.nf_calories
                    }
                }
                DailyData.newHistoryItem.kcalIn+=calories.toInt()
                Log.d("response", "${response.body()}")
            } else {
                Log.e("response", "Failed to get nutrition data: ${response.errorBody()}")

            }
            withContext(Dispatchers.Main){
                when (callDecider) {

                    CallDecider.BreakFast -> {
                        val oldCal = _breakfastCalories.value ?: 0.0
                        calories+=oldCal
                        _breakfastCalories.value = calories
                    }
                    CallDecider.Lunch -> {
                        val oldCal = _lunchCalories.value ?: 0.0
                        calories+=oldCal
                        _lunchCalories.value = calories
                    }
                    CallDecider.Dinner -> {
                        val oldCal = _dinnerCalories.value ?: 0.0
                        calories+=oldCal
                        _dinnerCalories.value = calories
                    }
                    else->calories=0.0
                }
            }


            Log.d("response", "calories $calories")
        }



     fun exercisCall(workout:String)=viewModelScope.launch(Dispatchers.IO) {
        val query=(NutritionixQuery(workout))
        val response=RetrofitClient.instance.getExerciseData(query)
        var calories=0

        if (response.isSuccessful){
            response.body()!!.exercises.let {
                it.forEach {
                    calories+=it.nf_calories
                }
            }
            DailyData.newHistoryItem.kcalOut+=calories
        }
        withContext(Dispatchers.Main){
            val oldCal=_workoutCalories.value
            calories+=oldCal!!
            _workoutCalories.value=calories
        }

    }


}