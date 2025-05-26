package com.example.journey.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.journey.databinding.ItemImageBinding
import java.io.File

class ImageAdapter(
    private val onDeleteClicked: (File) -> Unit,
    private val onImageClicked: (File) -> Unit
) : ListAdapter<File, ImageAdapter.ImageViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File) = oldItem.absolutePath == newItem.absolutePath
        override fun areContentsTheSame(oldItem: File, newItem: File) = oldItem == newItem
    }

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(file: File) {
            binding.imageView.setImageURI(Uri.fromFile(file))
            binding.deleteButton.setOnClickListener {
                onDeleteClicked(file)
            }
            binding.imageView.setOnClickListener {
                onImageClicked(file) // ✅ 여기서 전달
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
