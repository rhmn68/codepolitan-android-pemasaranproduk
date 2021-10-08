package com.codepolitan.pemasaranproduk.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.codepolitan.pemasaranproduk.data.repository.auth.AuthRepository

class LoginViewModel: ViewModel() {

    fun login(body: String) =
        AuthRepository.login(body).asLiveData()
}