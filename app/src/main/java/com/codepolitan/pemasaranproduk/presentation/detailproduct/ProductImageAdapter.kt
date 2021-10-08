package com.codepolitan.pemasaranproduk.presentation.detailproduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codepolitan.pemasaranproduk.databinding.ItemImageSliderBinding

class ProductImageAdapter: RecyclerView.Adapter<ProductImageAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemImageSliderBinding)
        :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemImageSliderBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = 5
}