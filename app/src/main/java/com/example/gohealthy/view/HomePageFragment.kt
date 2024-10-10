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

class HomePageFragment : Fragment() {

    private var waterCount: Int = 0

    lateinit var binding: FragmentHomePageBinding
    val stepsCounterVM:StepsCounterVM by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

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
        view.findViewById<AppCompatButton>(R.id.add_breakfast_button).setOnClickListener {
            val dialogFragment = AddBreakfastDialogFragment()
            dialogFragment.show(parentFragmentManager, "AddBreakfastDialog")
        }
        view.findViewById<AppCompatButton>(R.id.add_lunch_button).setOnClickListener {
            val dialogFragment = AddLunchDialogFragment()
            dialogFragment.show(parentFragmentManager, "AddLunchDialog")
        }
        view.findViewById<AppCompatButton>(R.id.add_dinner_button).setOnClickListener {
            val dialogFragment = AddDinnerDialogFragment()
            dialogFragment.show(parentFragmentManager, "AddDinnerDialog")
        }
        view.findViewById<AppCompatButton>(R.id.Add_workout_button).setOnClickListener {
            val dialogFragment = AddWorkoutDialogFragment()
            dialogFragment.show(parentFragmentManager, "AddWorkoutDialog")
        }

        val noWatersTextView = view.findViewById<TextView>(R.id.no_waters)
        val plusImg = view.findViewById<ImageView>(R.id.plus_img)
        val minusImg = view.findViewById<ImageView>(R.id.minus_img)

        noWatersTextView.text = waterCount.toString()

        plusImg.setOnClickListener {
            waterCount++
            noWatersTextView.text = waterCount.toString()
        }

        minusImg.setOnClickListener {
            if (waterCount > 0) {
                waterCount--
                noWatersTextView.text = waterCount.toString()
            }
        }

        return view
    }

    private val addBreakfastDialog: Dialog by lazy {
        Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_fragment_b)
        }
    }

    private val addLunchDialog: Dialog by lazy {
        Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_fragment_l)
        }
    }

    private val addDinnerDialog: Dialog by lazy {
        Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_fragment_d)
        }
    }

    private val addWorkoutDialog: Dialog by lazy {
        Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_fragment_workout)
        }
    }
}
