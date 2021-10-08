package com.codepolitan.pemasaranproduk.presentation.login

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.codepolitan.pemasaranproduk.R
import com.codepolitan.pemasaranproduk.data.hawkstorage.HawkStorage
import com.codepolitan.pemasaranproduk.data.model.Resource
import com.codepolitan.pemasaranproduk.data.model.auth.LoginRequest
import com.codepolitan.pemasaranproduk.databinding.ActivityLoginBinding
import com.codepolitan.pemasaranproduk.presentation.main.MainActivity
import com.codepolitan.pemasaranproduk.presentation.register.RegisterActivity
import com.codepolitan.pemasaranproduk.utils.*
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {
  
  private lateinit var binding: ActivityLoginBinding
  private lateinit var dialogLoading: AlertDialog
  private lateinit var loginViewModel: LoginViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    dialogLoading = showDialogLoading(this)

    onAction()
  }
  
  private fun onAction() {
    binding.btnLogin.setOnClickListener {
      val email = binding.etEmailLogin.text.toString().trim()
      val pass = binding.etPasswordLogin.text.toString().trim()

      if (checkValid(email, pass)){
        loginToServer(email, pass)
      }
    }
    
    binding.btnRegisterLogin.setOnClickListener { startActivity<RegisterActivity>() }
  }

  private fun loginToServer(email: String, pass: String) {
    val loginRequest = LoginRequest(
      email = email,
      password = pass
    )
    val body = Gson().toJson(loginRequest)

    loginViewModel.login(body).observe(this){ state ->
      when(state){
        is Resource.Empty -> {
          dialogLoading.dismiss()
          showDialogNotification(this, "EMPTY")
        }
        is Resource.Error -> {
          dialogLoading.dismiss()
          val errorMessage = state.errorMessage
          showDialogError(this, errorMessage)
        }
        is Resource.Loading -> {
          dialogLoading.show()
        }
        is Resource.Success -> {
          dialogLoading.dismiss()
          val dialogSuccess = showDialogSuccess(this, "Congratulation you are already logged in")
          dialogSuccess.show()

          val data = state.data
          HawkStorage.instance(this).setUser(data)

          Handler(Looper.getMainLooper())
            .postDelayed({
              dialogSuccess.dismiss()
              startActivity<MainActivity>()
              finish()
            }, 1200)
        }
      }
    }
  }

  private fun checkValid(email: String, pass: String): Boolean {
    return when{
      email.isEmpty() -> {
        binding.textInputEmailLogin.error = getString(R.string.field_email)
        binding.textInputEmailLogin.requestFocus()
        false
      }
      !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
        binding.textInputEmailLogin.error = getString(R.string.valid_email)
        binding.textInputEmailLogin.requestFocus()
        false
      }
      pass.isEmpty() -> {
        binding.textInputEmailLogin.error = null

        binding.textInputPasswordLogin.error = getString(R.string.field_password)
        binding.textInputPasswordLogin.requestFocus()
        false
      }
      else -> {
        binding.textInputEmailLogin.error = null
        binding.textInputPasswordLogin.error = null

        true
      }
    }
  }
}