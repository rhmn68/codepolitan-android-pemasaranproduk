package com.codepolitan.pemasaranproduk.presentation.myads

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.codepolitan.pemasaranproduk.databinding.ItemMyAdBinding

class MyAdsAdapter: RecyclerView.Adapter<MyAdsAdapter.ViewHolder>() {
  
  private var listener: ((View) -> Unit)? = null
  
  class ViewHolder(private val binding: ItemMyAdBinding)
    : RecyclerView.ViewHolder(binding.root){
    fun bindItem(listener: ((View) -> Unit)?) {
      binding.btnMoreMyAd.setOnClickListener { view->
        listener?.let {
          it(view)
        }
      }
    }
  
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = ItemMyAdBinding.inflate(layoutInflater, parent, false)
    return ViewHolder(binding)
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindItem(listener)
  }
  
  override fun getItemCount(): Int = 10
  
  fun onClick(listener: ((View) -> Unit)){
    this.listener = listener
  }
}