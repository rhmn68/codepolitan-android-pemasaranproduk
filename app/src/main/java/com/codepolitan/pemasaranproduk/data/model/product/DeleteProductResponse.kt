package com.codepolitan.pemasaranproduk.data.model.product


import com.google.gson.annotations.SerializedName

data class DeleteProductResponse(
    @SerializedName("message")
    val message: String? = null
)