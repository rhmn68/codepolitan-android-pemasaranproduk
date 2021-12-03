package com.codepolitan.pemasaranproduk.data.model.product


import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("currentPage")
    var currentPage: Int? = null,
    @SerializedName("data")
    val dataProduct: MutableList<DataProduct>? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("totalItems")
    val totalItems: Int? = null,
    @SerializedName("totalPages")
    var totalPages: Int? = null
)