package com.example.journey.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.journey.R
import java.io.File

class ImagePreviewDialog(private val imageFile: File) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity
        if (activity == null || activity.isFinishing || !isAdded) {
            Log.w("ImagePreviewDialog", "Activity is not ready, dialog cancelled")
            return super.onCreateDialog(savedInstanceState)
        }

        val dialog = Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_image_preview)

        val imageView = dialog.findViewById<ImageView>(R.id.previewImageView)

        if (imageFile.exists()) {
            try {
                Glide.with(requireContext().applicationContext)
                    .load(imageFile)
                    .placeholder(R.drawable.sampleimg)
                    .error(R.drawable.sampleimg)
                    .override(1080, 1920)
                    .into(imageView)
            } catch (e: Exception) {
                Log.e("ImagePreviewDialog", "Glide load failed", e)
                imageView.setImageResource(R.drawable.sampleimg)
            }
        } else {
            imageView.setImageResource(R.drawable.sampleimg)
        }

        imageView.setOnClickListener { dismiss() }

        return dialog
    }
}
