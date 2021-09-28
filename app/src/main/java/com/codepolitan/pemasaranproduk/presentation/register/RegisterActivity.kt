package com.codepolitan.pemasaranproduk.presentation.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codepolitan.pemasaranproduk.databinding.ActivityRegisterBinding
import com.codepolitan.pemasaranproduk.utils.toast

class RegisterActivity : AppCompatActivity() {
  
  private lateinit var binding: ActivityRegisterBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    initActionBar()
    onAction()
  }
  
  private fun onAction() {
    binding.btnRegister.setOnClickListener { toast("Register") }
    
    binding.tbRegister.setNavigationOnClickListener { onBackPressed() }
  }
  
  private fun initActionBar() {
    setSupportActionBar(binding.tbRegister)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = ""
  }
}