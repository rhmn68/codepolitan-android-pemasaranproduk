package com.codepolitan.pemasaranproduk.network

import com.codepolitan.pemasaranproduk.network.api.AdsService
import com.codepolitan.pemasaranproduk.network.api.AuthService
import com.codepolitan.pemasaranproduk.network.api.ProductService
import com.codepolitan.pemasaranproduk.network.api.ProfileService

object ApiService {
    fun getAuthService(): AuthService{
        return RetrofitClient.newInstance()
            .getRetrofitInstance()
            .create(AuthService::class.java)
    }

    fun productService(): ProductService{
        return RetrofitClient.newInstance()
            .getRetrofitInstance()
            .create(ProductService::class.java)
    }

    fun adsService(): AdsService{
        return RetrofitClient.newInstance()
            .getRetrofitInstance()
            .create(AdsService::class.java)
    }

    fun profileService(): ProfileService{
        return RetrofitClient.newInstance()
            .getRetrofitInstance()
            .create(ProfileService::class.java)
    }
}