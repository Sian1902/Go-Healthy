package com.example.gohealthy.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gohealthy.databinding.FragmentHistoryBinding
import com.example.gohealthy.helpers.PrefManager
import com.example.gohealthy.history.HistoryAdapter
import com.example.gohealthy.viewModel.HistoryVM


class HistoryFragment : Fragment() {
    private lateinit var prefManager: PrefManager
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter
    private val historyVM: HistoryVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        prefManager = PrefManager(requireContext())
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

     val  isDarkMode= prefManager.isDarkMode()

        binding.themeSwitchCompat.isChecked = isDarkMode

        binding.themeSwitchCompat.setOnCheckedChangeListener{ _, isChecked ->


                if (isChecked) {
                    // Enable dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    recreate(requireActivity())
                    prefManager.setDarkMode(true)
                } else {
                    // Enable light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    recreate(requireActivity())
                    prefManager.setDarkMode(false)
                }



        }



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





