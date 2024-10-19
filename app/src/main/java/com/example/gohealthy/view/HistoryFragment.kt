package com.example.gohealthy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gohealthy.databinding.FragmentHistoryBinding
import com.example.gohealthy.history.HistoryAdapter
import com.example.gohealthy.viewModel.HistoryVM


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter
    private val historyVM: HistoryVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Show progress while loading
        binding.progressBar.visibility = View.VISIBLE

        // Observe the ViewModel for the history list
        historyVM.liveHistoryList.observe(viewLifecycleOwner) { historyList ->

            // Hide progress bar when data is loaded
            binding.progressBar.visibility = View.GONE

            // If the history list is empty, show the empty message
            if (historyList.isEmpty()) {
                binding.emptyMessage.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyMessage.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE

                // Set up the adapter
                historyAdapter = HistoryAdapter(historyList)
                binding.recyclerView.adapter = historyAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    }





