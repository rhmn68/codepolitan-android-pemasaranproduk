package com.codepolitan.pemasaranproduk.data.model.product


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateAdsResponse(
    @SerializedName("data")
    val dataCreateAds: DataProduct? = null,
    @SerializedName("message")
    val message: String? = null
): Parcelable