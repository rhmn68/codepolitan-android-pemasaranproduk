package com.codepolitan.pemasaranproduk.data.model.profile


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("message")
    val message: String? = null
)