package com.codepolitan.pemasaranproduk.data.repository.profile

import com.codepolitan.pemasaranproduk.data.model.profile.ProfileResponse

class ProfileCacheDataSource {
    private var profileResponse: ProfileResponse? = null

    fun getProfile() = profileResponse

    fun saveProfile(profileResponse: ProfileResponse){
        this.profileResponse = profileResponse
    }
}