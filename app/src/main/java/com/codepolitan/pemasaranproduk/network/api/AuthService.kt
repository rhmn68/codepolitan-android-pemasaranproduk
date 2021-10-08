package com.codepolitan.pemasaranproduk.network.api

import com.codepolitan.pemasaranproduk.data.model.auth.LoginResponse
import com.codepolitan.pemasaranproduk.data.model.auth.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @Headers("Content-type: application/json")
    @POST("auth/login")
    suspend fun login(@Body body: String): Response<LoginResponse>

    @Headers("Content-type: application/json")
    @POST("auth/register")
    suspend fun register(@Body body: String): Response<RegisterResponse>
}