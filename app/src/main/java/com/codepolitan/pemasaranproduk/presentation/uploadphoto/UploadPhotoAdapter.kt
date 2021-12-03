package com.codepolitan.pemasaranproduk.presentation.uploadphoto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepolitan.pemasaranproduk.BuildConfig
import com.codepolitan.pemasaranproduk.data.model.product.ImageProduct
import com.codepolitan.pemasaranproduk.databinding.ItemUploadPhotoBinding
import com.codepolitan.pemasaranproduk.utils.disabled
import com.codepolitan.pemasaranproduk.utils.enabled
import com.codepolitan.pemasaranproduk.utils.gone
import com.codepolitan.pemasaranproduk.utils.visible

class UploadPhotoAdapter: RecyclerView.Adapter<UploadPhotoAdapter.ViewHolder>() {

    var images = ArrayList<ImageProduct?>()
        set(value) {
            if (images.size > 0){
                images.clear()
            }
            field.addAll(value)
            notifyItemRangeChanged(0, field.size)
        }

    private var listenerRemove: ((ImageProduct, Int) -> Unit)? = null
    private var listener: ((Int) -> Unit)? = null

    class ViewHolder(private val binding: ItemUploadPhotoBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(
            imageProduct: ImageProduct?,
            listener: ((Int) -> Unit)?,
            listenerRemove: ((ImageProduct, Int) -> Unit)?
        ) {
            val image = imageProduct?.image
            val preview = imageProduct?.preview

            if (imageProduct != null){
                showImage()
                clickRemove(listenerRemove, imageProduct)
                if (preview != null){
                    Glide.with(itemView)
                        .load(preview)
                        .into(binding.ivPhoto)
                }else{
                    val imageUrl = BuildConfig.BASE_URL_IMAGE + image
                    Glide.with(itemView)
                        .load(imageUrl)
                        .into(binding.ivPhoto)
                }
            }else{
                hideImage()
                clickItem(listener)
            }
        }

        private fun clickItem(listener: ((Int) -> Unit)?) {
            binding.container.setOnClickListener {
                if (listener != null){
                    listener(adapterPosition)
                }
            }
        }

        private fun clickRemove(
            listenerRemove: ((ImageProduct, Int) -> Unit)?,
            imageProduct: ImageProduct
        ) {
            binding.btnRemove.setOnClickListener {
                if (listenerRemove != null){
                    listenerRemove(imageProduct, adapterPosition)
                }
            }
        }

        private fun hideImage() {
            binding.containerImage.gone()
            binding.imageViewPhoto.visible()
            binding.btnRemove.gone()
            binding.container.enabled()
        }

        private fun showImage() {
            binding.containerImage.visible()
            binding.imageViewPhoto.gone()
            binding.btnRemove.visible()
            binding.container.disabled()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUploadPhotoBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(images[position], listener, listenerRemove)
    }

    override fun getItemCount(): Int = images.size

    fun onClick(listener: ((Int) -> Unit)){
        this.listener = listener
    }

    fun onClickRemove(listenerRemove: ((ImageProduct, Int) -> Unit)){
        this.listenerRemove = listenerRemove
    }

    fun remove(position: Int){
        images[position] = null
        notifyItemChanged(position)
    }

    fun updateItem(imageProduct: ImageProduct, position: Int){
        images[position] = imageProduct
        notifyItemChanged(position, imageProduct)
    }
}