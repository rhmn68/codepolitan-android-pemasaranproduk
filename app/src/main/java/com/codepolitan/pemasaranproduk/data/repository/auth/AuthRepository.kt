package com.codepolitan.pemasaranproduk.data.repository.auth

import com.codepolitan.pemasaranproduk.data.model.ApiResponse
import com.codepolitan.pemasaranproduk.data.model.Resource
import com.codepolitan.pemasaranproduk.data.model.auth.LoginResponse
import com.codepolitan.pemasaranproduk.data.model.auth.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

object AuthRepository {
    private val authRemoteDataSource = AuthRemoteDataSource()

    fun login(body: String): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading)

        when(val apiResponse = authRemoteDataSource.login(body).first()){
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(data))
            }

            is ApiResponse.Empty -> {
                emit(Resource.Empty)
            }

            is ApiResponse.Error -> {
                val errorMessage = apiResponse.errorMessage
                emit(Resource.Error(errorMessage))
            }
        }
    }

    fun register(body: String): Flow<Resource<RegisterResponse>> = flow {
        emit(Resource.Loading)

        when(val apiResponse = authRemoteDataSource.register(body).first()){
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(data))
            }

            is ApiResponse.Empty -> {
                emit(Resource.Empty)
            }

            is ApiResponse.Error -> {
                val errorMessage = apiResponse.errorMessage
                emit(Resource.Error(errorMessage))
            }
        }
    }
}