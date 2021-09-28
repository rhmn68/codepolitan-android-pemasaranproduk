package com.codepolitan.pemasaranproduk.presentation.splash

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.codepolitan.pemasaranproduk.R
import com.codepolitan.pemasaranproduk.presentation.login.LoginActivity
import com.codepolitan.pemasaranproduk.utils.startActivity

class SplashActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    
    delayAndGoToLogin()
  }
  
  private fun delayAndGoToLogin() {
    Handler(Looper.getMainLooper()).postDelayed({
      startActivity<LoginActivity>()
      finish()
      
      finishAffinity()
    },2000)
  }
}