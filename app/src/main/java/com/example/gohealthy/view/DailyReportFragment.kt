package com.example.gohealthy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.gohealthy.R
import com.example.gohealthy.databinding.FragmentDailyReportBinding
import com.example.gohealthy.helpers.DailyData


class DailyReportFragment : Fragment() {
    private lateinit var binding: FragmentDailyReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyReportBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.stepsText.text="${DailyData.oldHistoryItem.steps}"
        binding.kcalInText.text="${DailyData.oldHistoryItem.kcalIn}"
        binding.kcalOutText.text="${DailyData.oldHistoryItem.steps}"
        binding.backToHomeButton.setOnClickListener {
            findNavController().navigate(R.id.homePageFragment)
        }
    }


}