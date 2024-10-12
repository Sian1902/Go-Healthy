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
import com.google.firebase.auth.FirebaseAuth

class signinFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)

        // Set up sign-in button
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

            // Authenticate user with Firebase
            authenticateUser(email, password)
        }

        // Set up register now click listener
        binding.registerNowTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signinFragment_to_signUpFragment)
        }

        return binding.root
    }

    // Firebase Authentication Method
    private fun authenticateUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful, navigate to WelcomeFragment
                    findNavController().navigate(R.id.action_signinFragment_to_welcomeFragment)
                    Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                } else {
                    // Login failed
                    Toast.makeText(requireContext(), "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
