package com.example.gohealthy

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun isFirstTimeLaunch(): Boolean {
        return pref.getBoolean("is_first_time_launch", true)
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor.putBoolean("is_first_time_launch", isFirstTime)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean("is_logged_in", false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        editor.putBoolean("is_logged_in", isLoggedIn)
        editor.apply()
    }
}
