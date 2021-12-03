package com.codepolitan.pemasaranproduk.data.repository.product

import com.codepolitan.pemasaranproduk.data.model.product.ProductResponse

class ProductCacheDataSource {
    private var productResponse: ProductResponse? = null
    private var myAdsResponse: ProductResponse? = null

    fun saveDataProduct(productResponse: ProductResponse){
        this.productResponse = productResponse
    }

    fun getDataProduct() = productResponse

    fun saveMyAds(myAdsResponse: ProductResponse){
        this.myAdsResponse = myAdsResponse
    }

    fun getDataMyAds() = myAdsResponse
}