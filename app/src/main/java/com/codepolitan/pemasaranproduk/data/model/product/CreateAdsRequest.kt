package com.codepolitan.pemasaranproduk.data.model.product


import com.google.gson.annotations.SerializedName

data class CreateAdsRequest(
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
    @SerializedName("loc_latitude")
    val locLatitude: Double? = null,
    @SerializedName("loc_longitude")
    val locLongitude: Double? = null,
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