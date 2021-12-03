package com.codepolitan.pemasaranproduk.presentation.resultproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.codepolitan.pemasaranproduk.data.repository.ads.AdsRepository

class ResultProductViewModel: ViewModel() {
    fun findAds(lat: Double, lon: Double, title: String) =
        AdsRepository.findAds(lat, lon, title).asLiveData()
}