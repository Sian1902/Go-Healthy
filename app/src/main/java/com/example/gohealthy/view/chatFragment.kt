package com.example.gohealthy.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gohealthy.databinding.FragmentChatBinding
import com.example.gohealthy.viewModel.GeminiVM
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Pair<String, Boolean>>() // List to hold messages
    private lateinit var binding: FragmentChatBinding
    private val GeminiVM: GeminiVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)
        chatAdapter = ChatAdapter(messages)
        binding.chatRecyclerView.adapter = chatAdapter
        addWelcomeMessage()

        // Make sure input field gains focus and show keyboard
       binding.inputMessage.requestFocus()


        // Send button click listener
        binding.sendButton.setOnClickListener {
            val message = binding.inputMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message) // Send user message
                binding.inputMessage.text?.clear() // Clear input field
            } else {
                Toast.makeText(context, "Please type a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to add the first welcome message to the RecyclerView
    private fun addWelcomeMessage() {
        val welcomeMessage = "Hi! I'm here to help you achieve your fitness goals! 💪✨ Feel free to ask anything about fitness!"
        messages.add(Pair(welcomeMessage, false)) // false indicates it's from the system/chatbot
        chatAdapter.notifyItemInserted(messages.size - 1)
    }

    // Function to send the user message and simulate the bot response
    private fun sendMessage(message: String) {
        // Add user message to the list
        messages.add(Pair(message, true))
        chatAdapter.notifyItemInserted(messages.size - 1)

        val response=GeminiVM.getResponse(message)
        // Simulate bot response
        messages.add(response)
        chatAdapter.notifyItemInserted(messages.size - 1)
    }

    // Function to show the keyboard

}
