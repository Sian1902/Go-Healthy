package com.example.gohealthy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GeminiVM: ViewModel() {
    private val generativeModel =
        GenerativeModel(
            // Specify a Gemini model appropriate for your use case
            modelName = "gemini-1.5-flash",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey ="AIzaSyBlMiOBTury4lia044JZjmnY08bQ6jzLak")

    fun getResponse(prompt: String): Pair<String, Boolean> {
        val query="Act as a chatbot that only answer questions related to fitness and health. $prompt"
        var response: String
        runBlocking(Dispatchers.IO) {
         response = generativeModel.generateContent(query).text.toString()
        }
       return Pair(response, false)
    }
    fun translat(query: String): String {
        val prompt="translate this text from arabic to english and return the answer only. $query"
        var response: String
        runBlocking(Dispatchers.IO) {
            response = generativeModel.generateContent(prompt).text.toString()
        }
        return response
    }

    }

