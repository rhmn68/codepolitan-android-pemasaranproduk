package com.codepolitan.pemasaranproduk.data.model.product


import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageProduct(
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("file")
    val image: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("product_id")
    val productId: Int? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    val preview: Bitmap? = null,
    val uri: Uri? = null,
    val path: String? = null
): Parcelable