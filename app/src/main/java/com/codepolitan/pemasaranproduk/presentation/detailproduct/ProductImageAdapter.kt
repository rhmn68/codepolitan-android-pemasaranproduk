package com.codepolitan.pemasaranproduk.presentation.detailproduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepolitan.pemasaranproduk.BuildConfig
import com.codepolitan.pemasaranproduk.data.model.product.ImageProduct
import com.codepolitan.pemasaranproduk.databinding.ItemImageSliderBinding

class ProductImageAdapter: RecyclerView.Adapter<ProductImageAdapter.ViewHolder>() {

    var images = mutableListOf<ImageProduct>()
        set(value) {
            field = value
            notifyItemRangeChanged(0, field.size)
        }

    class ViewHolder(private val binding: ItemImageSliderBinding)
        :RecyclerView.ViewHolder(binding.root){
        fun bindItem(imageProduct: ImageProduct) {
            val imageUrl = BuildConfig.BASE_URL_IMAGE + imageProduct.image
            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(android.R.color.darker_gray)
                .into(binding.ivThumbnailProduct)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemImageSliderBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(images[position])
    }

    override fun getItemCount(): Int = images.size
}