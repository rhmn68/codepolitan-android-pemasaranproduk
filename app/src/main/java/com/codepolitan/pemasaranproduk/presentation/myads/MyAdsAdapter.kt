package com.codepolitan.pemasaranproduk.presentation.myads

import android.view.*
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepolitan.pemasaranproduk.BuildConfig
import com.codepolitan.pemasaranproduk.data.model.product.DataProduct
import com.codepolitan.pemasaranproduk.databinding.ItemMyAdBinding
import com.codepolitan.pemasaranproduk.utils.disabled
import com.codepolitan.pemasaranproduk.utils.enabled

class MyAdsAdapter: RecyclerView.Adapter<MyAdsAdapter.ViewHolder>() {

  private val differCallback = object : DiffUtil.ItemCallback<DataProduct>(){
    override fun areItemsTheSame(oldItem: DataProduct, newItem: DataProduct): Boolean =
      oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DataProduct, newItem: DataProduct): Boolean =
      oldItem == newItem
  }

  val differ = AsyncListDiffer(this, differCallback)
  private val dataProducts = mutableListOf<DataProduct>()

  private var listener: ((View, DataProduct, Int) -> Unit)? = null
  private var listenerSold: ((DataProduct,Int) -> Unit)? = null
  
  class ViewHolder(private val binding: ItemMyAdBinding)
    : RecyclerView.ViewHolder(binding.root){
    fun bindItem(
      listener: ((View, DataProduct, Int) -> Unit)?,
      listenerSold: ((DataProduct, Int) -> Unit)?,
      dataProduct: DataProduct
    ) {
      binding.tvTitleMyAd.text = dataProduct.title
      binding.tvPriceMyAd.text = dataProduct.price.toString()

      if (dataProduct.sold == true){
        disableButtonSold()
      }else{
        enableButtonSold()
      }

      val images = dataProduct.imageProducts
      if (images != null && images.isNotEmpty()){
        val image = images[0].image

        if (image != null){
          val thumbnail = BuildConfig.BASE_URL_IMAGE + image
          Glide.with(itemView)
            .load(thumbnail)
            .placeholder(android.R.color.darker_gray)
            .into(binding.ivThumbnailMyAd)
        }
      }

      binding.btnSoldMyAd.setOnClickListener {
        listenerSold?.let {
          it(dataProduct, adapterPosition)
        }
      }

      binding.btnMoreMyAd.setOnClickListener { view->
        listener?.let {
          it(view, dataProduct, adapterPosition)
        }
      }
    }

    private fun enableButtonSold() {
      binding.btnSoldMyAd.enabled()
    }

    private fun disableButtonSold() {
      binding.btnSoldMyAd.disabled()
    }

  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = ItemMyAdBinding.inflate(layoutInflater, parent, false)
    return ViewHolder(binding)
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindItem(listener, listenerSold, differ.currentList[position])
  }
  
  override fun getItemCount(): Int = differ.currentList.size
  
  fun onClick(listener: ((View, DataProduct, Int) -> Unit)){
    this.listener = listener
  }

  fun onClickSold(listenerSold: ((DataProduct, Int) -> Unit)){
    this.listenerSold = listenerSold
  }

  fun delete(position: Int){
    dataProducts.removeAt(position)
    differ.submitList(dataProducts)
    notifyItemRemoved(position)
    notifyItemRangeChanged(position, differ.currentList.size)
  }

  fun insertData(dataProducts: List<DataProduct>){
    differ.submitList(dataProducts)
    if (this.dataProducts.size > 0){
      this.dataProducts.clear()
    }
    this.dataProducts.addAll(dataProducts)
    notifyItemRangeChanged(0, dataProducts.size)
  }

  fun updateDataSold(position: Int){
    dataProducts[position].sold = true
    differ.submitList(dataProducts)
    notifyItemChanged(position)
  }
}