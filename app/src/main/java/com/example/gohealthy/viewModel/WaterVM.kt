package com.example.gohealthy.viewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WaterVM(): ViewModel() {
    private val _waterIntake = MutableLiveData<Int>(0)
    val waterIntake: LiveData<Int> = _waterIntake
    private lateinit var context: Context

    private lateinit var  sharedPreferences:SharedPreferences

    fun setContext(context: Context) {
        this.context = context
        sharedPreferences = context.getSharedPreferences("WaterIntake", Context.MODE_PRIVATE)
        loadWaterIntake()
    }
    fun incWater(){
        var water=_waterIntake.value?:0
        water++
        _waterIntake.value=water
        saveWaterIntake(water)
    }
    fun decWater(){
        var water=_waterIntake.value?:0
        if(water==0) return
        water--
        _waterIntake.value=water
        saveWaterIntake(water)
    }
    private fun saveWaterIntake(water: Int) {
        sharedPreferences.edit()
            .putInt("waterIntake", water)
            .apply()
    }

    fun loadWaterIntake() {
        val savedWaterIntake = sharedPreferences.getInt("waterIntake", 0)
        _waterIntake.value = savedWaterIntake
    }
}