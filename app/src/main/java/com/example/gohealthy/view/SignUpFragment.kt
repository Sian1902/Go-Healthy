package com.example.gohealthy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gohealthy.helpers.PrefManager
import com.example.gohealthy.R
import com.example.gohealthy.UserData.User
import com.example.gohealthy.databinding.FragmentSignupBinding
import com.example.gohealthy.viewModel.FirebaseVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var prefManager: PrefManager
    private lateinit var auth: FirebaseAuth
    private val firebaseVM: FirebaseVM by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // Initialize Firebase Auth
        prefManager = PrefManager(requireContext())

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Prevent going back
            }
        })
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
            findNavController().navigate(R.id.signupToSignin)
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
        val age = ageStr.toFloatOrNull()

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

        // Launch the sign-up process asynchronously using lifecycleScope
        viewLifecycleOwner.lifecycleScope.launch {
            binding.loading.visibility = View.VISIBLE
            firebaseVM.signUp(User(name, gender, weight, height, email, password, age))
            if (firebaseVM.status) {
                prefManager.saveEmail(email.lowercase())
                prefManager.setLoggedIn(true)
                findNavController().navigate(R.id.homePageFragment)

            } else {
                Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
                binding.loading.visibility = View.GONE

            }
        }
    }
}
