package com.example.gohealthy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gohealthy.R
import com.example.gohealthy.databinding.FragmentSigninBinding
import com.google.firebase.firestore.FirebaseFirestore

class signinFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance() // Initialize Firestore instance
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)

        binding.singUpButton.setOnClickListener {
            val email = binding.nameTextField.editText?.text.toString().trim()
            val password = binding.weightTextField.editText?.text.toString().trim()

            if (email.isEmpty()) {
                binding.nameTextField.error = "Please enter your email"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.weightTextField.error = "Please enter your password"
                return@setOnClickListener
            }

            // Check Firestore for the user credentials
            authenticateUser(email, password)
        }

        return binding.root
    }

    private fun authenticateUser(email: String, password: String) {
        db.collection("users")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    //  navigate to WelcomeFragment
                    findNavController().navigate(R.id.action_signinFragment_to_welcomeFragment)
                    Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                } else {
                    // No match found
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
