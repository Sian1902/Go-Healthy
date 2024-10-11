package com.example.gohealthy.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gohealthy.R

class ChatAdapter(private val messages: List<Pair<String, Boolean>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
    }

    override fun getItemViewType(position: Int): Int {
        // Check if the message is from the user or the bot
        return if (messages[position].second) VIEW_TYPE_USER else VIEW_TYPE_BOT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_message, parent, false)
            UserViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bot_message, parent, false)
            BotViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position].first
        if (holder is UserViewHolder) {
            holder.bind(message)
        } else if (holder is BotViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    // ViewHolder for User Messages
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userMessageText: TextView = itemView.findViewById(R.id.userMessageText)

        fun bind(message: String) {
            userMessageText.text = message
        }
    }

    // ViewHolder for Bot Messages
    class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val botMessageText: TextView = itemView.findViewById(R.id.botMessageText)

        fun bind(message: String) {
            botMessageText.text = message
        }
    }
}
