package com.codepolitan.pemasaranproduk.presentation.uploadphoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.codepolitan.pemasaranproduk.data.repository.product.ProductRepository
import okhttp3.MultipartBody

class UploadPhotoViewModel: ViewModel() {

    fun uploadImages(token: String, id: Int, images: List<MultipartBody.Part>) =
        ProductRepository.uploadImages(token, id, images).asLiveData()

    fun deleteImage(token: String, id: Int) =
        ProductRepository.deleteImage(token, id).asLiveData()
}