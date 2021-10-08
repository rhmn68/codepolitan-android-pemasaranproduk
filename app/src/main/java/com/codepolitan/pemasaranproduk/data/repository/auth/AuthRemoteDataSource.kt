package com.codepolitan.pemasaranproduk.data.repository.auth

import com.codepolitan.pemasaranproduk.data.model.ApiResponse
import com.codepolitan.pemasaranproduk.data.model.auth.LoginResponse
import com.codepolitan.pemasaranproduk.data.model.auth.RegisterResponse
import com.codepolitan.pemasaranproduk.network.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import java.lang.Exception

class AuthRemoteDataSource {

    suspend fun login(body: String): Flow<ApiResponse<LoginResponse>> = flow {
        try {
            val response = ApiService.getAuthService().login(body)
            if (response.isSuccessful){
                val data = response.body()
                if (data != null){
                    emit(ApiResponse.Success(data))
                }else{
                    emit(ApiResponse.Empty)
                }
            }else{
                val type = object : TypeToken<LoginResponse>(){}.type
                val errorResponse: LoginResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
                emit(ApiResponse.Error(errorResponse.message))
            }
        }catch (e: Exception){
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun register(body: String): Flow<ApiResponse<RegisterResponse>> = flow {
        try {
            val response = ApiService.getAuthService().register(body)
            if (response.isSuccessful){
                val data = response.body()
                if (data != null){
                    emit(ApiResponse.Success(data))
                }else{
                    emit(ApiResponse.Empty)
                }
            }else{
                val type = object : TypeToken<RegisterResponse>(){}.type
                val errorResponse: RegisterResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
                emit(ApiResponse.Error(errorResponse.message))
            }
        }catch (e: Exception){
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}