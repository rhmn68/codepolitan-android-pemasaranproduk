package com.codepolitan.pemasaranproduk.network.api

import com.codepolitan.pemasaranproduk.data.model.product.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ProductService {

    @Headers("Content-type: application/json")
    @POST("product")
    suspend fun createAds(
        @Header("authorization") token: String,
        @Body body: String
    ) : Response<CreateAdsResponse>

    @Headers("Content-type: application/json")
    @PATCH("product/{id}")
    suspend fun updateAds(
        @Header("authorization") token: String,
        @Body body: String,
        @Path("id") id: Int
    ) : Response<UpdateProductResponse>

    @Multipart
    @POST("product/{id}/upload")
    suspend fun uploadImages(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Part images: List<MultipartBody.Part>
    ): Response<UploadImageResponse>

    @DELETE("image/{id}")
    suspend fun deleteImage(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Response<DeleteImageResponse>

    @GET("product")
    suspend fun showAllProduct(
        @Header("authorization") token: String,
        @Query("page") page: Int,
        @Query("size") sizeData: Int = 10
    ) : Response<ProductResponse>

    @GET("product")
    suspend fun showMyAds(
        @Header("authorization") token: String,
        @Query("user_id") userId: Int,
        @Query("page") page: Int,
        @Query("size") sizeData: Int = 10
    ) : Response<ProductResponse>

    @DELETE("product/{id}")
    suspend fun deleteProduct(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Response<DeleteProductResponse>
}