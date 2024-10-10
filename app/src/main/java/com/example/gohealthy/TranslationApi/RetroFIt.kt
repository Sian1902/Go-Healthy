package com.example.gohealthy.TranslationApi/*import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
object RetrofitInstance {

    // The base URL of LibreTranslate
    private const val BASE_URL = "https://libretranslate.com/"

    // Optional: Create a logger to log request and response details
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Set up OkHttpClient with logging
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Create Retrofit instance
    val api: TranslationApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // Set the logging client
            .addConverterFactory(GsonConverterFactory.create())  // Convert JSON to Kotlin objects
            .build()
            .create(TranslationApiService::class.java)  // Create the API service
    }
}
*/