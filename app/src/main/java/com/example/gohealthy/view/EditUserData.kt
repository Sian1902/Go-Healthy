package com.example.gohealthy.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.gohealthy.R
import com.example.gohealthy.UserData.User
import com.example.gohealthy.databinding.FragmentEditUserDataBinding
import com.example.gohealthy.viewModel.FirebaseVM
import kotlinx.coroutines.launch

class EditUserData : DialogFragment() {
    private lateinit var binding: FragmentEditUserDataBinding
    private val firebaseVM: FirebaseVM by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the layout using FragmentEditUserDataBinding
        binding = FragmentEditUserDataBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_bg)
        binding.nameField.editText?.setText(firebaseVM.user.value?.name)
        binding.ageField.editText?.setText(firebaseVM.user.value?.age!!.toInt().toString())
        binding.heightField.editText?.setText(firebaseVM.user.value?.height.toString())
        binding.weightHeight.editText?.setText(firebaseVM.user.value?.weight.toString())
        binding.save.setOnClickListener {
            // Safely handle input validation and parsing
            val name =
                binding.nameField.editText?.text?.toString().takeIf { it?.isNotEmpty() == true }
                    ?: firebaseVM.user.value?.name

            val age = binding.ageField.editText?.text?.toString()?.toFloatOrNull()
                ?: firebaseVM.user.value?.age

            val height = binding.heightField.editText?.text?.toString()?.toFloatOrNull()
                ?: firebaseVM.user.value?.height

            val weight = binding.weightHeight.editText?.text?.toString()?.toFloatOrNull()
                ?: firebaseVM.user.value?.weight

            if (name != null && age != null && height != null && weight != null) {
                lifecycleScope.launch {
                    // Update user data through ViewModel
                    firebaseVM.updateUserData(
                        User(
                            name,
                            firebaseVM.user.value!!.gender,
                            weight,
                            height,
                            firebaseVM.user.value!!.email,
                            firebaseVM.user.value!!.password,
                            age
                        ), requireContext()
                    )
                    dismiss() // Close the dialog after saving
                }
            } else {
                // Handle invalid data case (optional: show an error message)
                Toast.makeText(requireContext(), "Please fill in valid data", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return dialog
    }
}

