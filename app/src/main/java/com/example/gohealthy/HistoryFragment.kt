package com.example.gohealthy

import HistoryAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gohealthy.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {
    lateinit var binding: FragmentHistoryBinding
    val historyList = listOf(
        HistoryItem("Sat 10th of October", 200, 150, 1000),
        HistoryItem("Mon 8th of September", 180, 120, 9000)
       , HistoryItem("Mon 8th of September", 180, 120, 9000)
       , HistoryItem("Mon 8th of September", 180, 120, 9000),
        HistoryItem("Mon 8th of September", 180, 120, 9000)
       , HistoryItem("Mon 8th of September", 180, 120, 9000)
      ,  HistoryItem("Mon 8th of September", 180, 120, 9000)
       , HistoryItem("Mon 8th of September", 180, 120, 9000)
        // Add more items
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding= FragmentHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HistoryAdapter(historyList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

    }


}