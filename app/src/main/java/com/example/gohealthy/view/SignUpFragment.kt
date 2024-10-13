package com.example.gohealthy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gohealthy.R
import com.example.gohealthy.UserData.User
import com.example.gohealthy.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // Initialize Firebase Auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        // Set the gender dropdown
        val gender = resources.getStringArray(R.array.Gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdownmenu, gender)
        binding.genderTextField.editText?.let { autoCompleteTextView ->
            (autoCompleteTextView as AutoCompleteTextView).setAdapter(arrayAdapter)
        }

        // Set the button click listener for sign-up
        binding.singUpButton.setOnClickListener {
            createUserAccount()
        }

        // Navigate to SignInFragment when "signin now" is clicked
        binding.singinrNowTextView.setOnClickListener {
            findNavController().navigate(R.id.welcomeToHomePage)
        }

        return binding.root
    }

    private fun createUserAccount() {
        val name = binding.nameTextField.editText?.text.toString().trim()
        val gender = binding.genderTextField.editText?.text.toString().trim().takeIf { it.isNotEmpty() }
        val weightStr = binding.weightTextField.editText?.text.toString().trim()
        val heightStr = binding.heightTextField.editText?.text.toString().trim()
        val email = binding.emailTextField.editText?.text.toString().trim()
        val password = binding.passwordTextField.editText?.text.toString().trim()
        val ageStr = binding.ageTextField.editText?.text.toString().trim()

        // Convert the weight, height, and age to numbers
        val weight = weightStr.toFloatOrNull()
        val height = heightStr.toFloatOrNull()
        val age = ageStr.toIntOrNull()

        // Validation
        if (name.isEmpty()) {
            binding.nameTextField.error = "Please enter your name"
            return
        }
        if (weight == null) {
            binding.weightTextField.error = "Please enter a valid weight"
            return
        }
        if (height == null) {
            binding.heightTextField.error = "Please enter a valid height"
            return
        }
        if (email.isEmpty()) {
            binding.emailTextField.error = "Please enter your email"
            return
        }
        if (password.isEmpty()) {
            binding.passwordTextField.error = "Please enter your password"
            return
        }
        if (age == null) {
            binding.ageTextField.error = "Please enter a valid age"
            return
        }

        // Create a user account with Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Save user data to Firestore
                    val user = User(name, gender, weight, height, email, password, age)
                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "User data saved successfully!", Toast.LENGTH_SHORT).show()
                            // Navigate to the welcome fragment after successful signup
                          //  findNavController().navigate(R.id.action_signUpFragment_to_signinFragment)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Registration failed
                    Toast.makeText(requireContext(), "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendVerificationEmail() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Verification email sent! Please check your inbox.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to send verification email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
