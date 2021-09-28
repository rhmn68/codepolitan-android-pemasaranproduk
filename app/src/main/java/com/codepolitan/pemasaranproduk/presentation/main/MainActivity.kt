package com.codepolitan.pemasaranproduk.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.codepolitan.pemasaranproduk.R
import com.codepolitan.pemasaranproduk.databinding.ActivityMainBinding
import com.codepolitan.pemasaranproduk.presentation.home.HomeFragment
import com.codepolitan.pemasaranproduk.presentation.myads.MyAdsFragment
import com.codepolitan.pemasaranproduk.presentation.user.UserFragment

class MainActivity : AppCompatActivity() {
  
  private lateinit var binding: ActivityMainBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    initBottomNav()
  }
  
  override fun onBackPressed() {
    val itemId = binding.btmNavMain.selectedItemId
    if (itemId == R.id.action_home){
      finish()
    }else{
      if (itemId == 0){
        openHomeFragment()
      }
      openHomeFragment()
    }
  }
  
  private fun initBottomNav() {
    binding.btmNavMain.setOnItemSelectedListener {
      when(it.itemId){
        R.id.action_home -> {
          openFragment(HomeFragment())
          return@setOnItemSelectedListener true
        }
        
        R.id.action_sell -> {
    
        }
        
        R.id.action_my_ads -> {
          openFragment(MyAdsFragment())
          return@setOnItemSelectedListener true
        }
  
        R.id.action_user -> {
          openFragment(UserFragment())
          return@setOnItemSelectedListener true
        }
      }
      return@setOnItemSelectedListener false
    }
    openHomeFragment()
  }
  
  private fun openHomeFragment() {
    binding.btmNavMain.selectedItemId = R.id.action_home
  }
  
  private fun openFragment(fragment: Fragment) {
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.frame_main, fragment)
      .addToBackStack(null)
      .commit()
  }
}