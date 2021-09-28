package com.codepolitan.pemasaranproduk.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codepolitan.pemasaranproduk.databinding.ItemProductBinding

class RecommendProductAdapter : RecyclerView.Adapter<RecommendProductAdapter.ViewHolder>() {
  
  private var listener : ((String) -> Unit)? = null
  
  class ViewHolder(private val binding: ItemProductBinding)
    : RecyclerView.ViewHolder(binding.root){
    fun bindItem(listener: ((String) -> Unit)?) {
      itemView.setOnClickListener {
        listener?.let {
          it("")
        }
      }
    }
  
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
    return ViewHolder(binding)
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindItem(listener)
  }
  
  override fun getItemCount(): Int = 10
  
  fun onClick(listener : ((String) -> Unit)){
    this.listener = listener
  }
}