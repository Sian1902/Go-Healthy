package com.example.gohealthy.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gohealthy.databinding.FragmentProfilePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfilePageFragment : Fragment() {

    // Firebase instances
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // View Binding
    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using View Binding
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch and populate user data
        fetchUserData()

        // Handle save button click
        /*
        binding.btnSave.setOnClickListener {
            saveUserData()
        }

         */
    }

    private fun fetchUserData() {
        val user = auth.currentUser
        val userId = user?.uid

        if (userId != null) {
            val userRef = firestore.collection("users").document(userId)

            userRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Populate the EditText fields with user data
                    binding.profileName.setText(document.getString("name"))
                    binding.profileAge.setText(document.getLong("age")?.toString())
                    binding.profileHeight.setText(document.getLong("height")?.toString())
                    binding.profileWeight.setText(document.getLong("weight")?.toString())
                    binding.profileGender.setText(document.getString("gender")?.toString())
                } else {
                    Toast.makeText(context, "No user data found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData() {
        val user = auth.currentUser
        val userId = user?.uid

        if (userId != null) {
            val userRef = firestore.collection("users").document(userId)

            val updatedUser = mapOf(
                "name" to binding.profileName.text.toString(),
                "age" to binding.profileAge.text.toString().toIntOrNull(),
                "height" to binding.profileHeight.text.toString().toIntOrNull(),
                "weight" to binding.profileWeight.text.toString().toIntOrNull(),
                "gender" to binding.profileGender.text.toString()
            )

            userRef.update(updatedUser)
                .addOnSuccessListener {
                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
