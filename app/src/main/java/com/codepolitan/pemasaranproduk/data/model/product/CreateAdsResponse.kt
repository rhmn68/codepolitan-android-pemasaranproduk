package com.codepolitan.pemasaranproduk.data.model.product


import com.google.gson.annotations.SerializedName

data class CreateAdsResponse(
    @SerializedName("data")
    val dataCreateAds: DataCreateAds? = null,
    @SerializedName("message")
    val message: String? = null
)