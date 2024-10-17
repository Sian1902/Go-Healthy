package com.example.gohealthy

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

class PrefManager(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun saveEmail(email: String){
        editor.putString("email", email)
        editor.apply()
    }
    fun loadEmail():String {
        val s:String =  pref.getString("email", "")?:""
        return s
    }

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

    fun saveImageLocally(imageUri: Uri) {
        editor.putString("profileImageUri", imageUri.toString())
        editor.apply()
    }

    fun loadSavedImage(): Uri? {
        val savedUri = pref.getString("profileImageUri", null)
        return savedUri?.let { Uri.parse(it) }
    }

}
