package com.codepolitan.pemasaranproduk.data.repository.ads

import com.codepolitan.pemasaranproduk.data.model.product.ProductResponse

class AdsCacheDataSource {
    private var findAdsResponse: ProductResponse? = null

    fun getDataFindAds() = findAdsResponse

    fun saveDataFindAds(findAdsResponse: ProductResponse?){
        this.findAdsResponse = findAdsResponse
    }
}