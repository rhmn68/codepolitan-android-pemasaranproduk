package com.codepolitan.pemasaranproduk.network

import com.codepolitan.pemasaranproduk.network.api.AuthService

object ApiService {
    fun getAuthService(): AuthService{
        return RetrofitClient.newInstance()
            .getRetrofitInstance()
            .create(AuthService::class.java)
    }
}