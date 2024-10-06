package com.example.gohealthy

import HistoryAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gohealthy.databinding.FragmentHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoryFragment : Fragment() {
    lateinit var binding: FragmentHistoryBinding
    val historyList = listOf(
        HistoryItem("Sat 10th of October", 200, 150, 1000),
        HistoryItem("Mon 8th of September", 180, 120, 9000)
       , HistoryItem("Mon 8th of September", 180, 120, 9000)
       , HistoryItem("Mon 8th of September", 180, 120, 9000),
        HistoryItem("Mon 8th of September", 180, 120, 9000)
       , HistoryItem("Mon 8th of September", 180, 120, 9000)
      ,  HistoryItem("Mon 8th of September", 180, 120, 9000)
       , HistoryItem("Mon 8th of September", 180, 120, 9000)
        // Add more items
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding= FragmentHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HistoryAdapter(historyList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        translateText("Hello, how are you?", "en", "es")
    }

    fun translateText(text: String, sourceLang: String, targetLang: String) {
        // Create the request object
        val request = TranslationRequest(
            q = text,
            source = sourceLang,
            target = targetLang
        )

        // Make the API call
        RetrofitInstance.api.translate(request).enqueue(object : Callback<TranslationResponse> {
            override fun onResponse(
                call: Call<TranslationResponse>,
                response: Response<TranslationResponse>
            ) {
                // Check if the response was successful
                if (response.isSuccessful) {
                    // Get the translated text
                    val translatedText = response.body()?.translatedText
                    Log.d("Translation","Translated Text: $translatedText")
                } else {
                    // Handle error
                    Log.e("Translation", "Failed to translate: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<TranslationResponse>, t: Throwable) {
                // Handle failure
                Log.e("Translation","Error: ${t.message}")
            }
        })
    }

}