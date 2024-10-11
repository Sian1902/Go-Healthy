package com.example.gohealthy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gohealthy.R

class ChatFragment : Fragment() {

    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Pair<String, Boolean>>() // List to hold messages

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.chatRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        chatAdapter = ChatAdapter(messages)
        recyclerView.adapter = chatAdapter

        // Setup message input and send button
        val inputMessage: EditText = view.findViewById(R.id.inputMessage)
        val sendButton: View = view.findViewById(R.id.sendButton)

        sendButton.setOnClickListener {
            val message = inputMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message) // Send user message
                inputMessage.text.clear() // Clear input field
            } else {
                Toast.makeText(context, "Please type a message", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun sendMessage(message: String) {
        // Add user message to the list
        messages.add(Pair(message, true))
        chatAdapter.notifyItemInserted(messages.size - 1)

        // Simulate a bot response
        receiveBotResponse()
    }

    private fun receiveBotResponse() {
        val botMessage = "This is a response from the bot." // Sample bot message
        messages.add(Pair(botMessage, false)) // Add bot message to the list
        chatAdapter.notifyItemInserted(messages.size - 1) // Notify adapter of new item
    }
}
