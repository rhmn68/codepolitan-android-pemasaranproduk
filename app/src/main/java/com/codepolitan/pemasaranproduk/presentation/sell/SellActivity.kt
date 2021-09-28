package com.codepolitan.pemasaranproduk.presentation.sell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codepolitan.pemasaranproduk.databinding.ActivitySellBinding

class SellActivity : AppCompatActivity() {
  
  private lateinit var binding: ActivitySellBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySellBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }
}