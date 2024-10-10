package com.example.gohealthy.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.gohealthy.R
import com.example.gohealthy.ViewModel.StepsCounterVM
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity(), SensorEventListener {
    val stepsCounterVM:StepsCounterVM by viewModels()
    private var running= false
    private var sensorManager: SensorManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        checkActivityRecognitionPermission()
        stepsCounterVM.loadData(this)
        stepsCounterVM.resetSteps(this)
        sensorManager=getSensorManager()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNavBar)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    override fun onResume() {
        super.onResume()
        running=true
        val stepSensor= sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if(stepSensor==null){
            Toast.makeText(this,"no sensor detected",Toast.LENGTH_LONG)
        }
        else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        //sensorManager?.unregisterListener(this)
    }
    private  fun checkActivityRecognitionPermission() {
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
            .setPositiveButton("Grant Permission") { _, _ ->
                requestActivityRecognitionPermission()
            }
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
    private  fun  getSensorManager():SensorManager{
        return this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (running && event != null) {
            stepsCounterVM.updateSteps(event.values[0].toInt())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}