package com.example.gohealthy.UserData

data class User(
    var name: String = "",
    val gender: String? = null,
    var weight: Float = 0.0f,
    var height: Float = 0.0f,
    val email: String = "",
    var password: String = "",
    var age: Float = 0.0f
)

