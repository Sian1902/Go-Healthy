package com.example.gohealthy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.example.gohealthy.databinding.FragmentSignupBinding
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import com.example.gohealthy.R
import com.example.gohealthy.UserData.User

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val db = FirebaseFirestore.getInstance()

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

        // Set the button click listener
        binding.singUpButton.setOnClickListener {
            saveUserData()
        }

        return binding.root
    }

    private fun saveUserData() {

        val name = binding.nameTextField.editText?.text.toString().trim()
        val gender = binding.genderTextField.editText?.text.toString().trim()
        val weight = binding.weightTextField.editText?.text.toString().trim()
        val height = binding.heightTextField.editText?.text.toString().trim()

        // Validate the fields ( make sure they are not empty)
        if (name.isEmpty()) {
            binding.nameTextField.error = "Please enter your name"
            return
        }
        if (gender.isEmpty()) {
            binding.genderTextField.error = "Please select your gender"
            return
        }
        if (weight.isEmpty()) {
            binding.weightTextField.error = "Please enter your weight"
            return
        }
        if (height.isEmpty()) {
            binding.heightTextField.error = "Please enter your height"
            return
        }

        // Create a User object using the data class
        val user = User(name, gender, weight, height)

        // Save data to Firebase Firestore
        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "User data saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

   /* // Method to retrieve user data from Firestore
    private fun getUserData(userId: String) {
        db.collection("users").document(userId).get().addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject(User::class.java)
            if (user != null) {
                // Use user data, for example:
                binding.nameTextField.editText?.setText(user.name)
                binding.genderTextField.editText?.setText(user.gender)
                binding.weightTextField.editText?.setText(user.weight)
                binding.heightTextField.editText?.setText(user.height)
            }
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "Failed to retrieve data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }*/
}
