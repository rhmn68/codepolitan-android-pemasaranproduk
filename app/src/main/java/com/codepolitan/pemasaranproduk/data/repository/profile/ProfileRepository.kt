package com.codepolitan.pemasaranproduk.data.repository.profile

import com.codepolitan.pemasaranproduk.data.model.ApiResponse
import com.codepolitan.pemasaranproduk.data.model.Resource
import com.codepolitan.pemasaranproduk.data.model.profile.ProfileResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

object ProfileRepository {
    private val profileRemoteDataSource = ProfileRemoteDataSource()
    private val profileCacheDataSource = ProfileCacheDataSource()

    fun getProfile(token: String): Flow<Resource<ProfileResponse>> = flow {
        emit(Resource.Loading)
        var profileResponse: ProfileResponse? = null

        try {
            profileResponse = profileCacheDataSource.getProfile()
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }

        if (profileResponse != null){
            emit(Resource.Success(profileResponse))
        }else{
            when(val apiResponse = profileRemoteDataSource.getProfile(token).first()){
                ApiResponse.Empty -> emit(Resource.Empty)
                is ApiResponse.Error -> {
                    val errorMessage = apiResponse.errorMessage
                    emit(Resource.Error(errorMessage))
                }
                is ApiResponse.Success -> {
                    val data = apiResponse.data
                    profileCacheDataSource.saveProfile(data)
                    emit(Resource.Success(data))
                }
            }
        }
    }
}