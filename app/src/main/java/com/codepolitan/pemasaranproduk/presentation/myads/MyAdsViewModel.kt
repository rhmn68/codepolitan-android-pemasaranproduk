package com.codepolitan.pemasaranproduk.presentation.myads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.codepolitan.pemasaranproduk.data.repository.product.ProductRepository

class MyAdsViewModel: ViewModel() {

    fun getMyAds(token: String, isSwipe: Boolean, userId: Int) =
        ProductRepository.showMyAds(token, isSwipe, userId).asLiveData()

    fun deleteProduct(token: String, id: Int) =
        ProductRepository.deleteProduct(token, id).asLiveData()

    fun updateProduct(token: String, body: String, idProduct: Int) =
        ProductRepository.updateAds(token, body, idProduct).asLiveData()
}