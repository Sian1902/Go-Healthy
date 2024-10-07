package com.example.gohealthy.UserData

data class User(
    val name: String = "",
    val gender: String? = null,
    val weight: Float = 0.0f,
    val height: Float = 0.0f,
    val email: String = "",
    val password: String = "",
    val age: Int = 0
)

