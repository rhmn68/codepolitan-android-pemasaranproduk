package com.codepolitan.pemasaranproduk.presentation.uploadphoto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codepolitan.pemasaranproduk.databinding.ActivityUploadPhotoBinding
import com.codepolitan.pemasaranproduk.presentation.main.MainActivity
import com.codepolitan.pemasaranproduk.utils.startActivity

class UploadPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadPhotoBinding
    private lateinit var uploadPhotoAdapter: UploadPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUploadPhotoAdapter()
        onAction()
    }

    private fun onAction() {
        binding.btnUploadPhoto.setOnClickListener {
            startActivity<MainActivity>()
            finishAffinity()
        }

        binding.tbUploadPhoto.setOnClickListener {
            startActivity<MainActivity>()
            finishAffinity()
        }
    }

    private fun initUploadPhotoAdapter() {
        uploadPhotoAdapter = UploadPhotoAdapter()
        binding.rvUploadPhoto.adapter = uploadPhotoAdapter
    }
}