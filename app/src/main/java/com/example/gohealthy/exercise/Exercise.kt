package com.example.gohealthy.exercise

import com.example.gohealthy.foodData.Photo

data class Exercise(
    val tag_id: Int,
    val user_input: String,
    val duration_min: Int,
    val met: Double,
    val nf_calories: Int,
    val photo: Photo,
    val compendium_code: Int,
    val name: String,
    val description: String?,
    val benefits: String?
)

