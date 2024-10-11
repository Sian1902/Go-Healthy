/*
package com.example.gohealthy.view

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gohealthy.R
import com.example.gohealthy.ViewModel.StepsCounterVM
import com.example.gohealthy.databinding.FragmentHomePageBinding
import com.example.gohealthy.databinding.FragmentWelcomeBinding
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import androidx.appcompat.widget.AppCompatButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gohealthy.NutritionixAPI.CallDecider
import com.example.gohealthy.ViewModel.NutritionixVM

class HomePageFragment : Fragment() {

    private var waterCount: Int = 0
    private val nutritionixVM:NutritionixVM by activityViewModels()
    private lateinit var binding: FragmentHomePageBinding
    private val stepsCounterVM:StepsCounterVM by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

       binding=FragmentHomePageBinding.inflate(inflater,container,false)
       stepsCounterVM.currentSteps.observe(viewLifecycleOwner){
           binding.circularProgressBar.progress=it.toFloat()
           binding.currentStepstext.text= "$it/"
       }
        nutritionixVM.breakFastCalories.observe(viewLifecycleOwner){
            binding.breakfastCal.text="${it.toInt()} KCal "
        }
        nutritionixVM.lunchCalories.observe(viewLifecycleOwner){
            binding.lunchCal.text="${it.toInt()} KCal "
        }
        nutritionixVM.dinnerCalories.observe(viewLifecycleOwner){
            binding.DinnerCal.text="${it.toInt()} KCal "
        }
        nutritionixVM.workoutCalories.observe(viewLifecycleOwner){
            binding.WorkoutCaloriesNo.text="${it.toInt()} KCal "
        }
       setCircularProgress()

        binding.addBreakfastButton.setOnClickListener {
            val dialogFragment = ApiCallPopUp(CallDecider.BreakFast)
            dialogFragment.show((activity as AppCompatActivity).supportFragmentManager,"breakfast")
        }
        binding.addLunchButton.setOnClickListener{
            val dialogFragment = ApiCallPopUp(CallDecider.Lunch)
            dialogFragment.show((activity as AppCompatActivity).supportFragmentManager,"Lunch")
        }
        binding.addDinnerButton.setOnClickListener{
            val dialogFragment = ApiCallPopUp(CallDecider.Dinner)
            dialogFragment.show((activity as AppCompatActivity).supportFragmentManager,"Dinner")
        }
        binding.AddWorkoutButton.setOnClickListener{
            val dialogFragment = ApiCallPopUp(CallDecider.Exercise)
            dialogFragment.show((activity as AppCompatActivity).supportFragmentManager,"Workout")
        }

        val noWatersTextView = view.findViewById<TextView>(R.id.no_waters)
        noWatersTextView.text = waterCount.toString()

        binding.plusImg.setOnClickListener {
            waterCount++
            noWatersTextView.text = waterCount.toString()
        }
        binding.minusImg.setOnClickListener {
            if (waterCount > 0) {
                waterCount--
                noWatersTextView.text = waterCount.toString()
            }
        }

        return binding.root // Return the inflated view

    }
   private fun setCircularProgress(){

        binding.circularProgressBar.apply {
            progress = 0f
            progressMax = 8000f
            progressBarColor = Color.rgb(230,97,4)
            backgroundProgressBarColor = Color.rgb(230,97,4)
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 3f // in DP
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
    }


    }

 */



