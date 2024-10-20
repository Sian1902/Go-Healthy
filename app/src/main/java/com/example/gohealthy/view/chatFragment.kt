package com.example.gohealthy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gohealthy.R
import com.example.gohealthy.databinding.FragmentChatBinding
import com.example.gohealthy.viewModel.GeminiVM

class ChatFragment : Fragment() {

    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Pair<String, Boolean>>() // List to hold messages
    private lateinit var binding: FragmentChatBinding
    private val GeminiVM: GeminiVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activity?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
        binding = FragmentChatBinding.inflate(inflater, container, false)

        // Initialize RecyclerView
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)
        chatAdapter = ChatAdapter(messages)
        binding.chatRecyclerView.adapter = chatAdapter

        // Add the first welcome message
        addWelcomeMessage()

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.homePageFragment)
        }

        binding.sendButton.setOnClickListener {
            val message = binding.inputMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message) // Send user message
                binding.inputMessage.text?.clear() // Clear input field
            } else {
                Toast.makeText(context, "Please type a message", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    // Function to add the first welcome message to the RecyclerView
    private fun addWelcomeMessage() {
        val welcomeMessage = "Hi! I'm here to help you achieve your fitness goals! 💪✨ Feel free to ask anything about fitness!"
        messages.add(Pair(welcomeMessage, false)) // false indicates it's from the system/chatbot
        chatAdapter.notifyItemInserted(messages.size - 1)
    }

    private fun sendMessage(message: String) {

        // Add user message to the list
        messages.add(Pair(message, true))
        chatAdapter.notifyItemInserted(messages.size - 1)
        // Simulate bot response
        messages.add(GeminiVM.getResponse(message))
        chatAdapter.notifyItemInserted(messages.size - 1)
    }
}

