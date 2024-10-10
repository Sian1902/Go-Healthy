package com.example.gohealthy.view

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.gohealthy.Exercise.Exercise
import com.example.gohealthy.Exercise.ExerciseData
import com.example.gohealthy.NutritionixAPI.NutritionixQuery
import com.example.gohealthy.NutritionixAPI.RetrofitClient
import com.example.gohealthy.ViewModel.StepsCounterVM
import com.example.gohealthy.databinding.FragmentWelcomeBinding
import com.example.gohealthy.foodData.NutritionData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WelcomeFragment : Fragment() {

    lateinit var binding: FragmentWelcomeBinding
    private val stepsCounterVM:StepsCounterVM by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)


        stepsCounterVM.currentSteps.observe(viewLifecycleOwner, Observer { steps ->
            binding.stepsText.text=steps.toString()
          })

        return binding.root
    }



}