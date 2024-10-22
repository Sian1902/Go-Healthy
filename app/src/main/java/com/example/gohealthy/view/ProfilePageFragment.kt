package com.example.gohealthy.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gohealthy.helpers.PrefManager
import com.example.gohealthy.R
import com.example.gohealthy.databinding.FragmentProfilePageBinding
import com.example.gohealthy.viewModel.FirebaseVM
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream
import java.util.Locale


class ProfilePageFragment : Fragment() {
    val firebaseVM: FirebaseVM by activityViewModels()
    private lateinit var prefManager: PrefManager
    private lateinit var binding: FragmentProfilePageBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentProfilePageBinding.inflate(layoutInflater)
        auth=FirebaseAuth.getInstance()
        prefManager= PrefManager(requireContext())

        setupThemeSwitch()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initializeLaunchers()
        loadSavedImage() // Load the saved image here

        binding.profileImg.setOnClickListener {
            // Open image selection options
            selectImage()
        }


        firebaseVM.user.observe(viewLifecycleOwner) { user ->
            // Check if user data is not null before updating UI
            user?.let {
                binding.profileName.text = it.name ?: "Unknown Name"
                binding.ageText.text = it.age?.toInt()?.toString() ?: "0"
                binding.heightText.text = "${it.height?.toInt() ?: 0} cm"
                binding.weightText.text = "${it.weight ?: 0.0} kg"
            }
        }
        if(Locale.getDefault().language=="ar"){
            binding.languageText.text=getString(R.string.english)
        }
        else{
            binding.languageText.text=getString(R.string.arabic)
            binding.languageText.text=getString(R.string.english)
        }
        binding.languageText.setOnClickListener {
            if(Locale.getDefault().language=="ar"){
              setlocale(requireContext(),"en")
                prefManager.saveLanguage("en")
              binding.languageText.text=getString(R.string.arabic)
            }
            else{
                setlocale(requireContext(),"ar")
                prefManager.saveLanguage("ar")
                binding.languageText.text=getString(R.string.english)
            }
            recreate(requireActivity())
        }
        binding.editBTN.setOnClickListener {
            val dialog=EditUserData()
            dialog.show((activity as AppCompatActivity).supportFragmentManager,"edit dialog")

        }
        binding.logoutBTN.setOnClickListener{
            auth.signOut()
            prefManager.saveImageLocally(Uri.EMPTY)
            prefManager.saveEmail("")
            prefManager.saveCaloriesIn(0)
            prefManager.saveCaloriesOut(0)

            findNavController().navigate(R.id.signinFragment)

        }


    }


    private fun setupThemeSwitch() {
        val isDarkMode = prefManager.isDarkMode()
        binding.themeSwitchCompat.isChecked = isDarkMode

        binding.themeSwitchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Enable dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                prefManager.setDarkMode(true)
            } else {
                // Enable light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                prefManager.setDarkMode(false)
            }
            // Recreate activity to apply theme change
            recreate(requireActivity())
        }
    }
    private fun setlocale(context: Context, lang:String){
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

    }



    private fun initializeLaunchers() {
        // Camera result launcher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                saveAndDisplayImage(imageBitmap)
            }
        }

        // Gallery result launcher
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    binding.profileImg.setImageURI(it)
                    saveImageLocally(it)
                }
            }
        }
    }

    private fun selectImage() {
        // Show options for camera or gallery
        // You can use a dialog or a bottom sheet to show options
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), 100)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(takePictureIntent)
        }
    }

    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(pickPhotoIntent)
    }

    private fun saveAndDisplayImage(bitmap: Bitmap) {
        val filename = "profile_image_${System.currentTimeMillis()}.png"
        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val uri = Uri.fromFile(file)
        binding.profileImg.setImageURI(uri)
        saveImageLocally(uri)
    }

    private fun saveImageLocally(imageUri: Uri) {
        // Logic to save image URI locally, e.g., using SharedPreferences or a database
        val prefManager = PrefManager(requireContext())
        prefManager.saveImageLocally(imageUri)
    }
    private fun loadSavedImage() {
        val prefManager = PrefManager(requireContext())
        val imageUri = prefManager.loadSavedImage()
        imageUri?.let {
            if (it != Uri.EMPTY){
            binding.profileImg.setImageURI(it)}
        }
    }



}