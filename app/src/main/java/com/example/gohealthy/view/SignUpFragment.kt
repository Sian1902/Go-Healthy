package com.example.gohealthy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.gohealthy.R
import com.example.gohealthy.databinding.FragmentSignupBinding


class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        val gender = resources.getStringArray(R.array.Gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdownmenu, gender)


        binding.genderTextField.editText?.let { autoCompleteTextView ->
            (autoCompleteTextView as AutoCompleteTextView).setAdapter(arrayAdapter)
        }




   
        return binding.root
    }

}