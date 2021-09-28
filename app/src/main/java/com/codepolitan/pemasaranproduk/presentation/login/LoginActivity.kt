package com.codepolitan.pemasaranproduk.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codepolitan.pemasaranproduk.R
import com.codepolitan.pemasaranproduk.databinding.ActivityLoginBinding
import com.codepolitan.pemasaranproduk.presentation.main.MainActivity
import com.codepolitan.pemasaranproduk.presentation.register.RegisterActivity
import com.codepolitan.pemasaranproduk.utils.startActivity

class LoginActivity : AppCompatActivity() {
  
  private lateinit var binding: ActivityLoginBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    onAction()
  }
  
  private fun onAction() {
    binding.btnLogin.setOnClickListener { startActivity<MainActivity>() }
    
    binding.btnRegisterLogin.setOnClickListener { startActivity<RegisterActivity>() }
  }
}