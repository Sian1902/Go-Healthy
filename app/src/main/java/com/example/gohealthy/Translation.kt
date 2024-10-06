package com.example.gohealthy

data class TranslationRequest(
    val q: String,     // The text to be translated
    val source: String, // Source language code (e.g., "en" for English)
    val target: String  // Target language code (e.g., "es" for Spanish)
)

// Data class for the response
data class TranslationResponse(
    val translatedText: String
)