package com.example.gohealthy.view

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

class HomePageFragment : Fragment() {
    lateinit var binding: FragmentHomePageBinding
    val stepsCounterVM:StepsCounterVM by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

       binding=FragmentHomePageBinding.inflate(inflater,container,false)
       stepsCounterVM.currentSteps.observe(viewLifecycleOwner){
           binding.circularProgressBar.progress=it.toFloat()
           binding.currentStepstext.text=it.toString()+" /"
       }


        binding.circularProgressBar.apply {
            // Set Progress
            progress = 0f


            // Set Progress Max
            progressMax = 8000f

            // Set ProgressBar Color
            progressBarColor = Color.rgb(230,97,4)
            // or with gradient
//            progressBarColorStart = Color.RED
//            progressBarColorEnd = Color.rgb(230,97,4)
//            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.rgb(230,97,4)
            // or with gradient
//            backgroundProgressBarColorStart = Color.WHITE
//            backgroundProgressBarColorEnd = Color.RED
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT

            // Set Width
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 3f // in DP

            // Other
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        return binding.root // Return the inflated view
    }
}
