package com.example.gohealthy.view

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gohealthy.R
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import androidx.appcompat.widget.AppCompatButton
import android.widget.ImageView
import android.widget.TextView

class HomePageFragment : Fragment() {

    private var waterCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        val circularProgressBar = view.findViewById<CircularProgressBar>(R.id.circularProgressBar)

        circularProgressBar.apply {
            progress = 0f
            setProgressWithAnimation(200f, 10000)
            progressMax = 200f
            progressBarColor = Color.rgb(230, 97, 4)
            backgroundProgressBarColor = Color.rgb(230, 97, 4)
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT
            progressBarWidth = 7f
            backgroundProgressBarWidth = 3f
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

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
