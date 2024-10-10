package com.example.gohealthy
data class HistoryItem(
    var id:String ="",
    val date: String="",
    val kcalIn: Int=0,
    val kcalOut: Int=0,
    val steps: Int=0
)
