package com.codepolitan.pemasaranproduk.data.model.product


import com.google.gson.annotations.SerializedName

data class UpdateProductRequest(
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("brand")
    val brand: String? = null,
    @SerializedName("category_id")
    val categoryId: Int? = null,
    @SerializedName("condition")
    val condition: Boolean? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("location_lat")
    val locationLat: String? = null,
    @SerializedName("location_long")
    val locationLong: String? = null,
    @SerializedName("model")
    val model: String? = null,
    @SerializedName("price")
    val price: Int? = null,
    @SerializedName("sold")
    val sold: Boolean? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("year")
    val year: String? = null
)