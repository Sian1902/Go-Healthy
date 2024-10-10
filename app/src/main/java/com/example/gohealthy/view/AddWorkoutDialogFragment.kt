package com.example.gohealthy.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.example.gohealthy.R

class AddWorkoutDialogFragment : DialogFragment() {

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
        val view = inflater.inflate(R.layout.dialog_fragment_workout, container, false)

        // Set up the close button listener
        view.findViewById<ImageView>(R.id.closeImg).setOnClickListener {
            dismiss() // Close the dialog when the close button is clicked
        }

        // Set up the save button listener
        view.findViewById<AppCompatButton>(R.id.save).setOnClickListener {
            // Handle the save action here (e.g., saving data)
            dismiss() // Close the dialog after saving
        }

        return view
    }
}
