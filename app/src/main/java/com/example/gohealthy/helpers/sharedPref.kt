package com.example.gohealthy.helpers

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

class PrefManager(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()
    fun saveCaloriesIn(calories: Int){
        editor.putInt("calories", calories)
        editor.apply()
    }
    fun loadCaloriesIn():Int {
        return pref.getInt("calories", 0)
    }
    fun loadSteps(): Float {
       return pref.getFloat("key1", 0f)

    }
    fun resetSteps(steps:Float) {
        editor.putFloat("key1", steps)
        editor.apply()
    }
    fun saveCaloriesOut(calories: Int){
        editor.putInt("caloriesOut", calories)
        editor.apply()
    }
    fun loadCaloriesOut():Int {
        return pref.getInt("caloriesOut", 0)
    }
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

    fun isDarkMode(): Boolean {
        return pref.getBoolean("isDarkMode", false)
    }

    fun setDarkMode(setDarkMode: Boolean) {
        editor.putBoolean("isDarkMode", setDarkMode)
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

    fun saveLanguage(language: String) {
        editor.putString("language", language)
        editor.apply()

    }
    fun loadLanguage(): String {
        return pref.getString("language", "en") ?: "en"
    }
}
