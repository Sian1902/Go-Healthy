package com.example.gohealthy.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gohealthy.R
import com.example.gohealthy.viewModel.StepsCounterVM
import com.example.gohealthy.databinding.FragmentHomePageBinding
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gohealthy.nutritionixAPI.CallDecider
import com.example.gohealthy.viewModel.NutritionixVM
import com.example.gohealthy.viewModel.WaterVM

class HomePageFragment : Fragment() {

    private val nutritionixVM:NutritionixVM by activityViewModels()
    private lateinit var binding: FragmentHomePageBinding
    private val stepsCounterVM:StepsCounterVM by activityViewModels()
    private val waterVM:WaterVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

       binding=FragmentHomePageBinding.inflate(inflater,container,false)
        waterVM.setContext(requireContext())
        return binding.root // Return the inflated view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepsCounterVM.currentSteps.observe(viewLifecycleOwner){
            binding.circularProgressBar.progress=it.toFloat()
            binding.currentStepstext.text= "$it/"
        }
        waterVM.waterIntake.observe(viewLifecycleOwner){
            binding.noWaters.text="$it"
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
        binding.chatButton.setOnClickListener{
            findNavController().navigate(R.id.homeToChat)
        }
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
        binding.minusImg.setOnClickListener{
            waterVM.decWater()
        }
        binding.plusImg.setOnClickListener{
            waterVM.incWater()
        }
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



