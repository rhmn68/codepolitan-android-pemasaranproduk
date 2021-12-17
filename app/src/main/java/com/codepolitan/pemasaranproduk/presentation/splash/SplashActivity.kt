package com.codepolitan.pemasaranproduk.presentation.splash

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.codepolitan.pemasaranproduk.R
import com.codepolitan.pemasaranproduk.data.hawkstorage.HawkStorage
import com.codepolitan.pemasaranproduk.presentation.login.LoginActivity
import com.codepolitan.pemasaranproduk.presentation.main.MainActivity
import com.codepolitan.pemasaranproduk.utils.startActivity

class SplashActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    
    delayAndGoToLogin()
  }
  
  private fun delayAndGoToLogin() {
    Handler(Looper.getMainLooper()).postDelayed({
      checkIsLogin()
    },2000)
  }

  private fun checkIsLogin(){
    val isLogin = HawkStorage.instance(this).isLogin()
    if (isLogin){
      startActivity<MainActivity>()
      finish()
    }else{
      startActivity<LoginActivity>()
      finish()
    }
  }
}