package com.codepolitan.pemasaranproduk.data.model.product


import com.google.gson.annotations.SerializedName

data class UpdateProductResponse(
    @SerializedName("message")
    val message: String? = null
)