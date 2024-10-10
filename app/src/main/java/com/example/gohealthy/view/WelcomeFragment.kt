package com.example.gohealthy.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gohealthy.Exercise.Exercise
import com.example.gohealthy.Exercise.ExerciseData
import com.example.gohealthy.NutritionixAPI.NutritionixQuery
import com.example.gohealthy.NutritionixAPI.RetrofitClient
import com.example.gohealthy.R
import com.example.gohealthy.databinding.FragmentWelcomeBinding
import com.example.gohealthy.foodData.NutritionData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WelcomeFragment : Fragment() {

    lateinit var binding: FragmentWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        binding.welcomeButton.setOnClickListener {
           findNavController().navigate(R.id.signUpFragment)
         nutritionCall()
            exerciseCall()

        }

        return binding.root
    }
    fun nutritionCall(){
        val nutritionQuery = NutritionixQuery("100 gram meat")
        val call = RetrofitClient.instance.getNutritionData(nutritionQuery)

        call.enqueue(object : Callback<NutritionData> {
            override fun onResponse(call: Call<NutritionData>, response: Response<NutritionData>) {
                if (response.isSuccessful) {
                    val nutritionData = response.body()
                    nutritionData?.let {
                        for (food in it.foods) {
                            Log.d("NutritionAPI", "Food: ${food.food_name}, Calories: ${food.nf_calories}, Protein: ${food.nf_protein}")
                        }
                    } ?: Log.e("NutritionAPI", "Response body is null")
                } else {
                    // Log the entire response body to get more details about the error
                    Log.e("NutritionAPI", "Response failed with code: ${response.code()} and message: ${response.message()}")
                    response.errorBody()?.let { errorBody ->
                        Log.e("NutritionAPI", "Error body: ${errorBody.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<NutritionData>, t: Throwable) {
                Log.e("NutritionAPI", "API call failed", t)
                t.printStackTrace()
            }
        })
    }
    fun exerciseCall() {
        val exerciseQuery = NutritionixQuery("ran for 1 hour")  // Use ExerciseQuery, not NutritionixQuery
        val call = RetrofitClient.instance.getExerciseData(exerciseQuery)

        call.enqueue(object : Callback<ExerciseData> {  // Expect ExerciseResponse here
            override fun onResponse(call: Call<ExerciseData>, response: Response<ExerciseData>) {
                if (response.isSuccessful) {
                    val exerciseData = response.body()
                    exerciseData?.let {
                        for (exercise in it.exercises) {
                            Log.d("ExerciseAPI", "Name: ${exercise.name}, Calories: ${exercise.nf_calories}, Duration: ${exercise.duration_min}")
                        }
                    } ?: Log.e("ExerciseAPI", "Response body is null")
                } else {
                    // Log the entire response body to get more details about the error
                    Log.e("ExerciseAPI", "Response failed with code: ${response.code()} and message: ${response.message()}")
                    response.errorBody()?.let { errorBody ->
                        Log.e("ExerciseAPI", "Error body: ${errorBody.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<ExerciseData>, t: Throwable) {
                Log.e("ExerciseAPI", "API call failed", t)
                t.printStackTrace()
            }
        })
    }

}