package com.example.gohealthy.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.example.gohealthy.R

class AddBreakfastDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create the dialog
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // Set the background to transparent
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this dialog
        val view = inflater.inflate(R.layout.dialog_fragment_b, container, false)

        view.findViewById<ImageView>(R.id.closeImg).setOnClickListener {
            dismiss()
        }

        view.findViewById<AppCompatButton>(R.id.save).setOnClickListener {
            dismiss()
        }

        return view
    }
}
