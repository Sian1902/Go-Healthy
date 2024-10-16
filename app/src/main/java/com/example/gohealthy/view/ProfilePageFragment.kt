package com.example.gohealthy.view

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.gohealthy.R
import com.example.gohealthy.databinding.FragmentProfilePageBinding
import com.example.gohealthy.viewModel.FirebaseVM
import java.util.Locale


class ProfilePageFragment : Fragment() {
    val firebaseVM: FirebaseVM by activityViewModels()
    private lateinit var binding: FragmentProfilePageBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfilePageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseVM.user.observe(viewLifecycleOwner){
            binding.profileName.text=it.name
            binding.ageText.text=it.age.toInt().toString()
            binding.heightText.text="${it.height.toInt()} cm"
            binding.weightText.text="${it.weight.toString()} kg"
        }
        if(Locale.getDefault().language=="ar"){
            binding.languageText.text=getString(R.string.english)
        }
        else{
            binding.languageText.text=getString(R.string.arabic)
            binding.languageText.text=getString(R.string.english)
        }
        binding.languageChanger.setOnClickListener {
            if(Locale.getDefault().language=="ar"){
              setlocale(requireContext(),"en")
              binding.languageText.text=getString(R.string.arabic)
            }
            else{
                setlocale(requireContext(),"ar")
                binding.languageText.text=getString(R.string.english)
            }
            recreate(requireActivity())
        }
        binding.editBTN.setOnClickListener {
            val dialog=EditUserData()
            dialog.show((activity as AppCompatActivity).supportFragmentManager,"edit dialog")
        }

    }
    fun setlocale(context: Context, lang:String){
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)

    }

}