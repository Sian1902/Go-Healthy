package com.example.gohealthy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gohealthy.R
import com.example.gohealthy.alarm.AlarmItem
import com.example.gohealthy.alarm.AndroidAlarmScheduler
import com.example.gohealthy.viewModel.StepsCounterVM
import com.example.gohealthy.databinding.FragmentWelcomeBinding
import java.time.LocalDateTime
import androidx.activity.OnBackPressedCallback
import com.example.gohealthy.helpers.PrefManager

class WelcomeFragment : Fragment() {
    private lateinit var prefManager: PrefManager

    lateinit var binding: FragmentWelcomeBinding
    private val stepsCounterVM:StepsCounterVM by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefManager = PrefManager(requireContext())

        // Handle back press to prevent going back to WelcomeFragment
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing to prevent going back
            }
        })

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {

        binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        binding.welcomeButton.setOnClickListener {
            prefManager.setFirstTimeLaunch(false)
            findNavController().navigate((R.id.welcomeToSignIn))
        }


        return binding.root
    }






}