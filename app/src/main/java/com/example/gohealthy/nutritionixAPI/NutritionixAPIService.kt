package com.example.gohealthy.nutritionixAPI

import com.example.gohealthy.exercise.ExerciseData
import com.example.gohealthy.foodData.NutritionData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NutritionixAPIService {

    @Headers(
        "x-app-id: e073c307",           // Replace with actual app ID if different
        "x-app-key: 47499142459d5e028e702ff273edbf5c",  // Replace with actual app key
        "Content-Type: application/json"
    )
    @POST("/v2/natural/nutrients")
    suspend fun getNutritionData(
        @Body nutritionQuery: NutritionixQuery
    ): Response<NutritionData>

    @Headers(
        "x-app-id: e073c307",           // Replace with actual app ID if different
        "x-app-key: 47499142459d5e028e702ff273edbf5c",  // Replace with actual app key
        "Content-Type: application/json"
    )
    @POST("/v2/natural/exercise")
    suspend fun getExerciseData(
    @Body exerciseQuery: NutritionixQuery
    ):Response<ExerciseData>
}
