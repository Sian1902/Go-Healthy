package com.example.gohealthy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gohealthy.helpers.PrefManager
import com.example.gohealthy.R
import com.example.gohealthy.alarm.AlarmItem
import com.example.gohealthy.alarm.AndroidAlarmScheduler
import com.example.gohealthy.databinding.FragmentSigninBinding
import com.example.gohealthy.viewModel.HistoryVM
import com.example.gohealthy.viewModel.FirebaseVM
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class signinFragment : Fragment() {
    private lateinit var prefManager: PrefManager
    private lateinit var binding: FragmentSigninBinding
    private lateinit var auth: FirebaseAuth
    private val firebaseVM: FirebaseVM by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        prefManager = PrefManager(requireContext())
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
            binding.registerNowTextView.setOnClickListener {
                   findNavController().navigate(R.id.signUpFragment)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                binding.loading.visibility = View.VISIBLE
                firebaseVM.signIn(email, password)
                if (firebaseVM.status) {
                    prefManager.setLoggedIn(true)
                    prefManager.saveEmail(email.lowercase())
                    val alarmItem= AlarmItem(LocalDateTime.now().withHour(23).withMinute(55),"test")
                    val scheduler= AndroidAlarmScheduler(requireContext())
                    alarmItem.let(scheduler::schedule)
                    findNavController().navigate(R.id.homePageFragment)

                } else {
                    binding.loading.visibility = View.GONE

                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }

        }

        // Set up register now click listener
        binding.registerNowTextView.setOnClickListener {
            findNavController().navigate(R.id.signinToSignUp)
        }

        return binding.root
    }


}
