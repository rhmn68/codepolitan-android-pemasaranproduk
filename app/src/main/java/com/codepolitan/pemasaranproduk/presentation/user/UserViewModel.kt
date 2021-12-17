package com.codepolitan.pemasaranproduk.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.codepolitan.pemasaranproduk.data.repository.profile.ProfileRepository

class UserViewModel: ViewModel() {
    fun getProfile(token: String) =
        ProfileRepository.getProfile(token).asLiveData()
}