package com.example.gohealthy.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gohealthy.databinding.HistoryItemBinding
import com.google.firebase.firestore.FirebaseFirestore

class HistoryAdapter(
    private val historyList: MutableList<HistoryItem>,
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItem) {
            binding.Date.text = item.date
            binding.KcalIn.text = item.kcalIn.toString()
            binding.KcalOut.text = item.kcalOut.toString()
            binding.Steps.text = item.steps.toString()


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = historyList.size
}
