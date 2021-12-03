package com.codepolitan.pemasaranproduk.network.api

import com.codepolitan.pemasaranproduk.data.model.product.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AdsService {

    @GET("ads/search")
    suspend fun findAdsByLocation(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("title") title: String
    ): Response<ProductResponse>
}