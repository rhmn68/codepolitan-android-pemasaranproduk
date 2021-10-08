package com.codepolitan.pemasaranproduk.presentation.uploadphoto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codepolitan.pemasaranproduk.databinding.ItemUploadPhotoBinding

class UploadPhotoAdapter: RecyclerView.Adapter<UploadPhotoAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemUploadPhotoBinding)
        : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUploadPhotoBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = 8
}