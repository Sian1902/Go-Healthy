package com.example.gohealthy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gohealthy.PrefManager
import com.example.gohealthy.R
import com.example.gohealthy.databinding.FragmentSigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class signinFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        prefManager = PrefManager(requireContext())

        // Handle back press to prevent going back to signinFragment
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing to prevent going back
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

            // Authenticate user with Firebase
            authenticateUser(email, password)
        }

        return binding.root
    }

    // Firebase Authentication Method
    private fun authenticateUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User signed in successfully
                    prefManager.setLoggedIn(true)
                    findNavController().navigate(R.id.historyFragment)
                } else {
                    // Sign in failed, show a message to the user
                    handleAuthenticationError(task.exception)
                }
            }
    }

    private fun handleAuthenticationError(exception: Exception?) {
        val message = when (exception) {
            is FirebaseAuthInvalidUserException -> "No account found with this email."
            is FirebaseAuthInvalidCredentialsException -> "Invalid password. Please try again."
            else -> "Authentication failed. Please try again."
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
