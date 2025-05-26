package com.example.journey.data.activity.share

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.journey.R
import com.example.journey.databinding.ActivityImageshareBinding
import com.example.journey.adapter.ImageAdapter
import com.example.journey.ui.dialog.ImagePreviewDialog
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class ImageshareActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageshareBinding
    private lateinit var imageAdapter: ImageAdapter
    private val imageFiles = mutableListOf<File>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageshareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 시스템 바 여백 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""

        setupRecyclerView()
        loadImagesFromInternalStorage()

        binding.selectImageButton.setOnClickListener {
            selectImagesFromGallery()
        }
    }

    private fun setupRecyclerView() {
        imageAdapter = ImageAdapter(
            onDeleteClicked = { file ->
                file.delete()
                imageFiles.remove(file)
                imageAdapter.submitList(imageFiles.toList())
            },
            onImageClicked = { file ->
                if (!isFinishing && !supportFragmentManager.isStateSaved) {
                    val dialog = ImagePreviewDialog(file)
                    dialog.show(supportFragmentManager, "imagePreview")
                }
            }
        )
        binding.imageRecyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.imageRecyclerView.adapter = imageAdapter
    }

    private val multiImagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            uris.forEach { uri ->
                val file = saveImageToInternalStorage(uri)
                if (file != null) {
                    imageFiles.add(file)
                }
            }
            imageAdapter.submitList(imageFiles.toList())
        }

    private fun selectImagesFromGallery() {
        multiImagePickerLauncher.launch("image/*")
    }

    private fun saveImageToInternalStorage(uri: Uri): File? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val file = File(filesDir, "img_${UUID.randomUUID()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadImagesFromInternalStorage() {
        imageFiles.clear()
        val files = filesDir.listFiles { file -> file.name.endsWith(".jpg") }
        if (files != null) {
            imageFiles.addAll(files)
        }
        imageAdapter.submitList(imageFiles.toList())
    }
}
