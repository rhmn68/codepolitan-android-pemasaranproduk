package com.codepolitan.pemasaranproduk.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.codepolitan.pemasaranproduk.data.repository.product.ProductRepository

class HomeViewModel: ViewModel() {

    fun getDataProducts(token: String, isSwipe: Boolean) =
        ProductRepository.showProduct(token, isSwipe).asLiveData()
}