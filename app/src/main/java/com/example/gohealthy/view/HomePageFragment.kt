package com.example.gohealthy.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gohealthy.R
import com.example.gohealthy.viewModel.StepsCounterVM
import com.example.gohealthy.databinding.FragmentHomePageBinding
import androidx.appcompat.app.AppCompatActivity
import com.example.gohealthy.alarm.AlarmItem
import com.example.gohealthy.alarm.AndroidAlarmScheduler
import com.example.gohealthy.nutritionixAPI.CallDecider
import com.example.gohealthy.viewModel.FirebaseVM
import com.example.gohealthy.viewModel.NutritionixVM
import java.time.LocalDateTime

class HomePageFragment : Fragment() {

    private val nutritionixVM:NutritionixVM by activityViewModels()
    private lateinit var binding: FragmentHomePageBinding
    private val stepsCounterVM:StepsCounterVM by activityViewModels()
    private val firebaseVM: FirebaseVM by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment



       binding=FragmentHomePageBinding.inflate(inflater,container,false)

        return binding.root // Return the inflated view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nutritionixVM.loadCal(requireContext())
        stepsCounterVM.currentSteps.observe(viewLifecycleOwner){
            binding.circularProgressBar.progress=it.toFloat()
            binding.stepCount.text= "$it ${getString(R.string.Steps)}"
        }
        nutritionixVM.totalCal.observe(viewLifecycleOwner){
            binding.toalcalories.text="${it.toInt()}"+" ${getString(R.string.kcal)}"
            binding.caloriesCircularProgressBar.progress=it.toFloat()
        }
        binding.addMealBTN.setOnClickListener {
            val dialogFragment = ApiCallPopUp(CallDecider.FOOD)
            dialogFragment.show((activity as AppCompatActivity).supportFragmentManager,"Meal")
        }
        binding.addExerciseBTN.setOnClickListener {
            val dialogFragment = ApiCallPopUp(CallDecider.EXERCISE)
            dialogFragment.show((activity as AppCompatActivity).supportFragmentManager,"Exercise")
        }
        firebaseVM.user.observe(viewLifecycleOwner){
            val totalCal=it.weight*2.205f*15
            val hSquared=(it.height/100.0f)*(it.height/100.0f)
            val BMI=it.weight/hSquared
            binding.name.text=" "+it.name
            binding.dailyCaloriesText.text="${totalCal.toInt()}"

            binding.BMItext.text=String.format("%.2f", BMI)

            when{
                BMI<18.5 -> binding.BMIRangeText.text=getString(R.string.underweight)
                BMI in 18.5..24.9 -> binding.BMIRangeText.text=getString(R.string.normal)
                BMI in 25.0..29.9 -> binding.BMIRangeText.text=getString(R.string.overweight)
                BMI>30 -> binding.BMIRangeText.text=getString(R.string.obese)
            }

            binding.caloriesCircularProgressBar.progressMax=totalCal

        }


    }


}



