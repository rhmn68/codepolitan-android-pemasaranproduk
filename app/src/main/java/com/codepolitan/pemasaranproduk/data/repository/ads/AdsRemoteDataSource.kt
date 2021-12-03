package com.codepolitan.pemasaranproduk.data.repository.ads

import com.codepolitan.pemasaranproduk.data.model.ApiResponse
import com.codepolitan.pemasaranproduk.data.model.product.ProductResponse
import com.codepolitan.pemasaranproduk.network.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AdsRemoteDataSource {

    suspend fun findAds(lat: Double, lon: Double, title: String): Flow<ApiResponse<ProductResponse>> = flow {
        try {
            val response = ApiService.adsService().findAdsByLocation(lat, lon, title)
            if (response.isSuccessful){
                val data = response.body()
                if (data != null){
                    emit(ApiResponse.Success(data))
                }else{
                    emit(ApiResponse.Empty)
                }
            }else{
                val type = object : TypeToken<ProductResponse>(){}.type
                val errorResponse: ProductResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
                emit(ApiResponse.Error(errorResponse.message.toString()))
            }
        }catch (t: Throwable){
            emit(ApiResponse.Error(t.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

}