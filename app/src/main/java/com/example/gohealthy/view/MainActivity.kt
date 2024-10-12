package com.example.gohealthy.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.gohealthy.PrefManager
import com.example.gohealthy.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private var backPressedTime: Long = 0 // For tracking double back press to exit
    private lateinit var toast: Toast
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge layout
        enableEdgeToEdge()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        prefManager = PrefManager(this)
        val currentUser = auth.currentUser

        // Set the content view to the activity_main layout
        setContentView(R.layout.activity_main)

        // Set up the NavHostFragment and NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Navigation based on user state
        when {
            prefManager.isFirstTimeLaunch() -> {
                navController.navigate(R.id.welcomeFragment)
            }
            !prefManager.isLoggedIn() || currentUser == null -> {
                navController.navigate(R.id.signinFragment)
            }
            else -> {
                navController.navigate(R.id.historyFragment)
            }
        }

        // Handle back pressed events
        handleBackPress()
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentTime = System.currentTimeMillis()

                if (currentTime - backPressedTime < 2000) {
                    toast.cancel() // Dismiss toast before closing app
                    finish() // Close the app
                } else {
                    toast = Toast.makeText(this@MainActivity, "Press back again to exit", Toast.LENGTH_SHORT)
                    toast.show()
                    backPressedTime = currentTime // Update back press time
                }
            }
        })
    }
}
