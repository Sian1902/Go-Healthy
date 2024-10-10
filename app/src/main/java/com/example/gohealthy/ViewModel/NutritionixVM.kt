package com.example.gohealthy.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gohealthy.NutritionixAPI.CallDecider
import com.example.gohealthy.NutritionixAPI.NutritionixQuery
import com.example.gohealthy.NutritionixAPI.RetrofitClient

class NutritionixVM:ViewModel() {
    private var _breakfastCalories= MutableLiveData(0.0)
    private var _lunchCalories= MutableLiveData(0.0)
    private var _dinnerCalories= MutableLiveData(0.0)
    private var _workoutCalories= MutableLiveData(0)

    val breakFastCalories:MutableLiveData<Double> get() = _breakfastCalories
    val lunchCalories:MutableLiveData<Double> get() = _lunchCalories
    val dinnerCalories:MutableLiveData<Double> get() = _dinnerCalories
    val workoutCalories:MutableLiveData<Int> get() = _workoutCalories
    suspend fun foodCall(food: String,callDecider: CallDecider){
        Log.d("response","functioned called")
        val query= NutritionixQuery(food)
        val response=RetrofitClient.instance.getNutritionData(query)
        var calories=0.0

        if(response.isSuccessful){
            response.body()!!.foods.let {
                it.forEach {
                    calories+=it.nf_calories
                }
            }
            Log.d("response","${response.body()}")
        }
        when(callDecider){
            CallDecider.BreakFast->_breakfastCalories.value!!.plus(calories)
            CallDecider.Lunch->_lunchCalories.value!!.plus(calories)
            CallDecider.Dinner->_dinnerCalories.value!!.plus(calories)
            else-> return
        }
        Log.d("response", " calories ${_breakfastCalories.value}")

    }
    suspend fun exercisCall(workout:String){
        val query=(NutritionixQuery(workout))
        val response=RetrofitClient.instance.getExerciseData(query)
        var calories=0

        if (response.isSuccessful){
            response.body()!!.exercises.let {
                it.forEach {
                    calories+=it.nf_calories
                }
            }
        }
        _workoutCalories.value!!.plus(calories)
    }
}