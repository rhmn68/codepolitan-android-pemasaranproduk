package com.codepolitan.pemasaranproduk.data.repository.product

import com.codepolitan.pemasaranproduk.data.model.ApiResponse
import com.codepolitan.pemasaranproduk.data.model.Resource
import com.codepolitan.pemasaranproduk.data.model.product.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import java.lang.Exception

object ProductRepository {
    private val productRemoteDataSource = ProductRemoteDataSource()
    private val productCacheDataSource = ProductCacheDataSource()
    private var pageProduct = 0
    private var myAdsPage = 0

    fun createAds(token: String, body: String) : Flow<Resource<CreateAdsResponse>> = flow {
        emit(Resource.Loading)

        when(val apiResponse = productRemoteDataSource.createAds(token, body).first()){
            ApiResponse.Empty -> { emit(Resource.Empty) }
            is ApiResponse.Error -> {
                val errorMessage = apiResponse.errorMessage
                emit(Resource.Error(errorMessage))
            }
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(data))
            }
        }
    }

    fun updateAds(token: String, body: String, idProduct: Int) : Flow<Resource<UpdateProductResponse>> = flow {
        emit(Resource.Loading)

        when(val apiResponse = productRemoteDataSource.updateAds(token, body, idProduct).first()){
            ApiResponse.Empty -> { emit(Resource.Empty) }
            is ApiResponse.Error -> {
                val errorMessage = apiResponse.errorMessage
                emit(Resource.Error(errorMessage))
            }
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(data))
            }
        }
    }

    fun uploadImages(token: String, id: Int, images: List<MultipartBody.Part>) : Flow<Resource<UploadImageResponse>> = flow {
        emit(Resource.Loading)

        when(val apiResponse = productRemoteDataSource.uploadImages(token, id, images).first()){
            ApiResponse.Empty -> { emit(Resource.Empty) }
            is ApiResponse.Error -> {
                val errorMessage = apiResponse.errorMessage
                emit(Resource.Error(errorMessage))
            }
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(data))
            }
        }
    }

    fun deleteImage(token: String, idProduct: Int) : Flow<Resource<DeleteImageResponse>> = flow {
        emit(Resource.Loading)

        when(val apiResponse = productRemoteDataSource.deleteImage(token, idProduct).first()){
            ApiResponse.Empty -> { emit(Resource.Empty) }
            is ApiResponse.Error -> {
                val errorMessage = apiResponse.errorMessage
                emit(Resource.Error(errorMessage))
            }
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(data))
            }
        }
    }

    fun showProduct(token: String, isSwipe: Boolean): Flow<Resource<ProductResponse>> = flow {
        emit(Resource.Loading)
        var productResponse: ProductResponse? = null

        try {
            //Product cache tidak kosong di halaman 2
            productResponse = productCacheDataSource.getDataProduct()
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }

        if (isSwipe){
            productResponse = null
            productResponse?.dataProduct?.clear()
            pageProduct = 0
        }

        val apiResponse = productRemoteDataSource.showAllProduct(token, pageProduct).first()

        //Data awal null
        if (productResponse != null){
            when(apiResponse){
                ApiResponse.Empty -> emit(Resource.Empty)
                is ApiResponse.Error -> {
                    val errorMessage = apiResponse.errorMessage
                    emit(Resource.Error(errorMessage))
                }
                is ApiResponse.Success -> {
                    val data = apiResponse.data
                    //berasal dari product cache
                    val oldData = productResponse.dataProduct
                    //berasal dari api
                    val newData = data.dataProduct

                    if (newData != null && newData.isNotEmpty()){
                        oldData?.addAll(newData)
                        productResponse.currentPage = data.currentPage
                        productResponse.totalPages = data.totalPages
                        pageProduct++
                    }

                    productCacheDataSource.saveDataProduct(productResponse)
                    emit(Resource.Success(productResponse))
                }
            }
        }else{
            //Kalo data awal null
            when(apiResponse){
                ApiResponse.Empty -> emit(Resource.Empty)
                is ApiResponse.Error -> {
                    val errorMessage = apiResponse.errorMessage
                    emit(Resource.Error(errorMessage))
                }
                is ApiResponse.Success -> {
                    val data = apiResponse.data
                    productCacheDataSource.saveDataProduct(data)
                    emit(Resource.Success(data))
                    pageProduct++
                }
            }
        }
    }

    fun deleteProduct(token: String, id: Int) : Flow<Resource<DeleteProductResponse>> = flow {
        emit(Resource.Loading)

        when(val apiResponse = productRemoteDataSource.deleteProduct(token, id).first()){
            ApiResponse.Empty -> { emit(Resource.Empty) }
            is ApiResponse.Error -> {
                val errorMessage = apiResponse.errorMessage
                emit(Resource.Error(errorMessage))
            }
            is ApiResponse.Success -> {
                val data = apiResponse.data
                emit(Resource.Success(data))
            }
        }
    }

    fun showMyAds(token: String, isSwipe: Boolean, userId: Int): Flow<Resource<ProductResponse>> = flow {
        emit(Resource.Loading)
        var productResponse: ProductResponse? = null

        try {
            //Product cache tidak kosong di halaman 2
            productResponse = productCacheDataSource.getDataMyAds()
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }

        if (isSwipe){
            productResponse = null
            productResponse?.dataProduct?.clear()
            myAdsPage = 0
        }

        val apiResponse = productRemoteDataSource.showMyAds(token, userId, myAdsPage).first()

        //Data awal null
        if (productResponse != null){
            when(apiResponse){
                ApiResponse.Empty -> emit(Resource.Empty)
                is ApiResponse.Error -> {
                    val errorMessage = apiResponse.errorMessage
                    emit(Resource.Error(errorMessage))
                }
                is ApiResponse.Success -> {
                    val data = apiResponse.data
                    //berasal dari product cache
                    val oldData = productResponse.dataProduct
                    //berasal dari api
                    val newData = data.dataProduct

                    if (newData != null && newData.isNotEmpty()){
                        oldData?.addAll(newData)
                        productResponse.currentPage = data.currentPage
                        productResponse.totalPages = data.totalPages
                        myAdsPage++
                    }

                    productCacheDataSource.saveMyAds(productResponse)
                    emit(Resource.Success(productResponse))
                }
            }
        }else{
            //Kalo data awal null
            when(apiResponse){
                ApiResponse.Empty -> emit(Resource.Empty)
                is ApiResponse.Error -> {
                    val errorMessage = apiResponse.errorMessage
                    emit(Resource.Error(errorMessage))
                }
                is ApiResponse.Success -> {
                    val data = apiResponse.data
                    productCacheDataSource.saveMyAds(data)
                    emit(Resource.Success(data))
                    myAdsPage++
                }
            }
        }
    }
}