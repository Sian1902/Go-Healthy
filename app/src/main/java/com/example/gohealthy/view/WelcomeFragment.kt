package com.example.gohealthy.view

import android.content.Context
import android.content.Intent
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
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.activity.OnBackPressedCallback
import com.example.gohealthy.PrefManager
import java.util.Locale

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
        binding.lang.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            if (activity != null) activity?.finish() else throw NullPointerException("Expression 'activity' must not be null")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var alarmItem= AlarmItem(LocalDateTime.now().plusSeconds(10),"test")
        val scheduler= AndroidAlarmScheduler(requireContext())
       // alarmItem.let(scheduler::schedule)

    }





}