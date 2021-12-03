package com.codepolitan.pemasaranproduk.data.model.product


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataProduct(
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("brand")
    val brand: String? = null,
    @SerializedName("category_id")
    val categoryId: Int? = null,
    @SerializedName("condition")
    val condition: Boolean? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("images")
    val imageProducts: List<ImageProduct>? = null,
    @SerializedName("loc_latitude")
    val locLatitude: String? = null,
    @SerializedName("loc_longitude")
    val locLongitude: String? = null,
    @SerializedName("model")
    val model: String? = null,
    @SerializedName("price")
    val price: Int? = null,
    @SerializedName("sold")
    var sold: Boolean? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    @SerializedName("user_id")
    val userId: Int? = null,
    @SerializedName("year")
    val year: String? = null
): Parcelable