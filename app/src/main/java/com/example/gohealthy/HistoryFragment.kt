package com.example.gohealthy

import HistoryAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gohealthy.databinding.FragmentHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoryFragment : Fragment() {
    private val auth = FirebaseAuth.getInstance()
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter
    private val historyList = mutableListOf<HistoryItem>()

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
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.logOutButton.setOnClickListener {
            logOut()
        }
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            fetchHistoryData(it)
        }
    }

    private fun  logOut(){
        auth.signOut()
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

        findNavController().navigate(R.id.signinFragment)
    }

    private fun fetchHistoryData(userId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userId)
            .collection("history")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val historyItem = document.toObject(HistoryItem::class.java)
                    historyItem.id = document.id // Store the document ID in the HistoryItem
                    historyList.add(historyItem)
                }
                historyAdapter = HistoryAdapter(historyList, userId) // Pass userId to the adapter
                binding.recyclerView.adapter = historyAdapter
            }
            .addOnFailureListener { exception ->
                Log.w("HistoryFragment", "Error getting documents: ", exception)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
