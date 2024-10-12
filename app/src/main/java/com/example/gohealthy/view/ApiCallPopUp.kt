package com.example.gohealthy.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import com.example.gohealthy.nutritionixAPI.CallDecider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.gohealthy.R
import com.example.gohealthy.viewModel.NutritionixVM
import com.example.gohealthy.databinding.FragmentApiCallPopUpBinding

class ApiCallPopUp(val callDecider: CallDecider) : DialogFragment() {
    private lateinit var binding: FragmentApiCallPopUpBinding
    private val nutritionixVM:NutritionixVM by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the layout using FragmentPopUpBinding
        binding = FragmentApiCallPopUpBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_bg)
        decidePopUp()
        binding.save.setOnClickListener{
            val query=binding.inputField.editText!!.text.toString()

                if(callDecider!=CallDecider.Exercise){
                    nutritionixVM.foodCall(query,callDecider)
                }
                else{
                    nutritionixVM.exercisCall(query)
                }

              dismiss()
        }
        return dialog
    }
    private fun decidePopUp(){
        when(callDecider){
            CallDecider.BreakFast->{
                binding.title.text=getString(R.string.add_your_breakfast)
                binding.inputField.hint= getString(R.string.breakfastHint)
            }
            CallDecider.Lunch->{
                binding.title.text=getString(R.string.add_your_lunch)
                binding.inputField.hint= getString(R.string.lunchHint)

            }
            CallDecider.Dinner->{
                binding.title.text=getString(R.string.add_your_dinner)
                binding.inputField.hint= getString(R.string.dinnerHint)

            }
            CallDecider.Exercise->{
                binding.title.text=getString(R.string.add_your_workout)
                binding.inputField.hint= getString(R.string.workoutHint)
            }

        }
    }
}