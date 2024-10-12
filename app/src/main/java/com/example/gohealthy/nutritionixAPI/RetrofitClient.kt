package com.example.gohealthy.nutritionixAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val Base_URL = "https://trackapi.nutritionix.com"
    val instance: NutritionixAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NutritionixAPIService::class.java)
    }
}
