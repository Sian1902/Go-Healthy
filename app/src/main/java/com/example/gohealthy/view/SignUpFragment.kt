package com.example.gohealthy.view

import android.os.Bundle
import android.util.Log
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
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // Initialize Firebase Auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        // Set up the gender dropdown
        val gender = resources.getStringArray(R.array.Gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdownmenu, gender)
        binding.genderTextField.editText?.let { autoCompleteTextView ->
            (autoCompleteTextView as AutoCompleteTextView).setAdapter(arrayAdapter)
        }

        // Set the button click listener
        binding.singUpButton.setOnClickListener {
            createUserAccount()
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

        val weight = weightStr.toFloatOrNull()
        val height = heightStr.toFloatOrNull()
        val age = ageStr.toIntOrNull()

        // Validation
        if (name.isEmpty() || weight == null || height == null || email.isEmpty() || password.isEmpty() || age == null) {
            Toast.makeText(requireContext(), "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a user account with Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get the user's unique ID
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    // Save user data to Firestore
                    val user = User(name, gender, weight, height, email, password, age)
                    db.collection("users").document(userId).set(user)
                        .addOnSuccessListener {
                            // Initialize the 'history' subcollection for the user
                          //  initializeHistorySubcollection(userId)

                            // After successful signup, navigate to welcome screen
                            findNavController().navigate(R.id.signinFragment)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun initializeHistorySubcollection(userId: String) {
        // Create an initial entry in the 'history' subcollection
        db.collection("users").document(userId).collection("history")
            .add(hashMapOf(
                "date" to "Today", // Example initial values
                "kcalIn" to 0,
                "kcalOut" to 0,
                "steps" to 0
            ))
            .addOnSuccessListener {
                Log.d("Firestore", "Initial history entry created successfully")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error creating initial history entry", e)
            }
    }
}
