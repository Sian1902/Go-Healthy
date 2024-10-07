package com.example.gohealthy.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gohealthy.R
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class HomePageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        // Access the CircularProgressBar using the inflated view
        val circularProgressBar = view.findViewById<CircularProgressBar>(R.id.circularProgressBar)

        circularProgressBar.apply {
            // Set Progress
            progress = 0f
            // or with animation
            setProgressWithAnimation(200f, 10000) // =1s // Awel parameter bey2ol eno hayewsal l 200f b3d 10 seconds

            // Set Progress Max
            progressMax = 200f

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

        return view // Return the inflated view
    }
}
