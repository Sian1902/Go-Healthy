package com.example.gohealthy.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gohealthy.helpers.DailyData
import com.example.gohealthy.helpers.PrefManager
import com.example.gohealthy.nutritionixAPI.CallDecider
import com.example.gohealthy.nutritionixAPI.NutritionixQuery
import com.example.gohealthy.nutritionixAPI.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NutritionixVM:ViewModel() {
    private var _caloriesIn=0.0f
    private var _caloriesOut=0
    private var _totalCal=MutableLiveData<Int>(0)
    val totalCal:MutableLiveData<Int> get() = _totalCal
    lateinit var prefManager: PrefManager

    fun loadCal(context: Context){
        prefManager= PrefManager(context)
        _caloriesIn=prefManager.loadCaloriesIn().toFloat()
        _caloriesOut=prefManager.loadCaloriesOut()
        _totalCal.value=(_caloriesIn-_caloriesOut).toInt()
    }
    fun saveCal(context: Context){
        prefManager= PrefManager(context)
        prefManager.saveCaloriesIn(_caloriesIn.toInt())
        prefManager.saveCaloriesOut(_caloriesOut)
    }
     fun foodCall(food: String, callDecider: CallDecider,context: Context) =viewModelScope.launch(Dispatchers.IO) {
            Log.d("response", "function called")
            val query = NutritionixQuery(food)

            val response = RetrofitClient.instance.getNutritionData(query)


            if (response.isSuccessful && response.body() != null) {
                response.body()!!.foods.let { foods ->
                    foods.forEach {
                        _caloriesIn += it.nf_calories.toFloat()
                    }
                }
                DailyData.newHistoryItem.kcalIn=_caloriesIn.toInt()
                withContext(Dispatchers.Main){

                    _totalCal.value=(_caloriesIn-_caloriesOut).toInt()
                }
                saveCal(context)
            } else {
                Log.e("response", "Failed to get nutrition data: ${response.errorBody()}")

            }



        }



     fun exercisCall(workout:String,context: Context)=viewModelScope.launch(Dispatchers.IO) {
        val query=(NutritionixQuery(workout))
        val response=RetrofitClient.instance.getExerciseData(query)


        if (response.isSuccessful){
            response.body()!!.exercises.let {
                it.forEach {
                    _caloriesOut+=it.nf_calories
                }
            }
            DailyData.newHistoryItem.kcalOut=_caloriesOut
            withContext(Dispatchers.Main){
                    _totalCal.value=(_caloriesIn-_caloriesOut).toInt()


            }
            saveCal(context)
        }


    }


}