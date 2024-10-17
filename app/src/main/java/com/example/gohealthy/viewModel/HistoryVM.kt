package com.example.gohealthy.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gohealthy.history.HistoryAdapter
import com.example.gohealthy.history.HistoryItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoryVM:ViewModel() {
    private lateinit var historyAdapter: HistoryAdapter
    private val historyList = mutableListOf<HistoryItem>()
    private val auth = FirebaseAuth.getInstance()
    private var _liveHistoryList = MutableLiveData<MutableList<HistoryItem>>()
    val liveHistoryList get() = _liveHistoryList


     suspend fun fetchHistoryData() {
        val userId = auth.currentUser?.uid ?: return
         historyList.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userId)
            .collection("history")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val historyItem = document.toObject(HistoryItem::class.java)
                    historyList.add(historyItem)
                }
                _liveHistoryList.value = historyList
            }
            .addOnFailureListener { exception ->
                Log.w("HistoryFragment", "Error getting documents: ", exception)
            }
    }

}