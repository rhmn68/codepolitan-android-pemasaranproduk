package com.codepolitan.pemasaranproduk.data.repository.profile

import com.codepolitan.pemasaranproduk.data.model.ApiResponse
import com.codepolitan.pemasaranproduk.data.model.profile.ProfileResponse
import com.codepolitan.pemasaranproduk.network.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProfileRemoteDataSource {
    suspend fun getProfile(token: String): Flow<ApiResponse<ProfileResponse>> = flow {
        try {
            val response = ApiService.profileService().profile(token)
            if (response.isSuccessful){
                val data = response.body()
                if (data != null){
                    emit(ApiResponse.Success(data))
                }else{
                    emit((ApiResponse.Empty))
                }
            }else{
                val type = object : TypeToken<ProfileResponse>(){}.type
                val errorResponse: ProfileResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
                emit(ApiResponse.Error(errorResponse.message.toString()))
            }
        }catch (t: Throwable){
            emit(ApiResponse.Error(t.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}