package com.example.gohealthy.foodData

data class Tags(
    val item: String,
    val measure: String?,
    val quantity: String,
    val food_group: Int,
    val tag_id: Int
)