package com.example.gohealthy.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import com.example.gohealthy.nutritionixAPI.CallDecider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.gohealthy.R
import com.example.gohealthy.viewModel.NutritionixVM
import com.example.gohealthy.databinding.FragmentApiCallPopUpBinding
import com.example.gohealthy.viewModel.GeminiVM

class ApiCallPopUp(val callDecider: CallDecider) : DialogFragment() {
    private lateinit var binding: FragmentApiCallPopUpBinding
    private val nutritionixVM:NutritionixVM by activityViewModels()
    private val geminiVM:GeminiVM by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the layout using FragmentPopUpBinding
        binding = FragmentApiCallPopUpBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_bg)
        decidePopUp()
        binding.save.setOnClickListener{

                var query=binding.inputField.editText!!.text.toString()
                query= geminiVM.translat(query)
                if(callDecider!=CallDecider.EXERCISE){
                    nutritionixVM.foodCall(query,callDecider,requireContext())
                }
                else{
                    nutritionixVM.exercisCall(query,requireContext())
                }

              dismiss()
        }
        return dialog
    }
    private fun decidePopUp(){
        when(callDecider){
            CallDecider.FOOD->{
                binding.title.text=getString(R.string.add_your_breakfast)
                binding.inputField.hint= getString(R.string.breakfastHint)
            }

            CallDecider.EXERCISE->{
                binding.title.text=getString(R.string.add_your_workout)
                binding.inputField.hint= getString(R.string.workoutHint)
            }

        }
    }
}