package com.example.gohealthy.view

import android.Manifest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.gohealthy.PrefManager
import com.example.gohealthy.R
import com.example.gohealthy.viewModel.HistoryVM
import com.example.gohealthy.notification.NotificationService
import com.example.gohealthy.viewModel.FirebaseVM
import com.google.firebase.FirebaseApp
import com.example.gohealthy.viewModel.StepsCounterVM
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var prefManager: PrefManager
    private var backPressedTime: Long = 0 // For tracking double back press to exit
    private lateinit var toast: Toast
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val stepsCounterVM: StepsCounterVM by viewModels()
    private var running = false
    private var sensorManager: SensorManager? = null
    private val firebaseVM: FirebaseVM by viewModels()
    private val historyVM: HistoryVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        createNotificationChannel()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        prefManager = PrefManager(this)
        val currentUser = auth.currentUser
        // Set the content view to the activity_main layout
        setContentView(R.layout.activity_main)

        // Check permissions and manage battery optimizations
        checkActivityRecognitionPermission()
        stepsCounterVM.loadData(this)
        stepsCounterVM.resetSteps(this)
        sensorManager = getSensorManager()

        // Set up navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        when {
            prefManager.isFirstTimeLaunch() -> {
                navController.navigate(R.id.welcomeFragment)
            }
            !prefManager.isLoggedIn() || currentUser == null -> {
                navController.navigate(R.id.signinFragment)
            }
            else -> {
                //replace with main screen
                navController.navigate(R.id.homePageFragment)
            }
        }

        // Handle back pressed events
        handleBackPress()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNavBar)



        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Hide/show bottom navigation based on fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.welcomeFragment, R.id.signUpFragment, R.id.chatFragment, R.id.signinFragment,R.id.dailyReportFragment-> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

        // Check if the intent contains the extra to open the DailyReportFragment
        if (intent?.getStringExtra("openFragment") == "DailyReport") {
            navController.navigate(R.id.dailyReportFragment)  // Use navigation component to navigate to the fragment
        }

        // Manage battery optimizations
        requestBatteryOptimizationExemption()
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
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
         val channel= NotificationChannel(
            NotificationService.DAILY_REPORTS_CHANNEL_ID,
            "Daily Report",
            NotificationManager.IMPORTANCE_HIGH
         )
            channel.description="Notifies you that yor daily report is generated"
            val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun requestBatteryOptimizationExemption() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName = packageName
            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager

            // Check if the app is already ignoring battery optimizations
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                // Request to ignore battery optimizations
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }
    private fun openDailyReportFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, DailyReportFragment())
        fragmentTransaction.commit()
    }
    override fun onResume() {
        super.onResume()
        prefManager.loadEmail()
        lifecycleScope.launch {
           historyVM.fetchHistoryData()
            firebaseVM.getUser(prefManager.loadEmail())

        }

        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "No step counter sensor detected", Toast.LENGTH_LONG).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregister the sensor listener when the activity is paused
        sensorManager?.unregisterListener(this)
    }

    private fun checkActivityRecognitionPermission() {
        // Check if the permission is already granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Show a dialog to explain why the permission is needed
            showPermissionExplanationDialog()
        }
    }

    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("This app needs access to your activity data to count your steps. Please grant this permission to continue.")
            .setPositiveButton("Grant Permission") { _, _ -> requestActivityRecognitionPermission() }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun requestActivityRecognitionPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
            1001 // You can use any request code
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission was denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getSensorManager(): SensorManager {
        return this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running && event != null) {
            stepsCounterVM.updateSteps(event.values[0].toInt())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
