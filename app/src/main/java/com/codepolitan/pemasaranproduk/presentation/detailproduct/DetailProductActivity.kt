package com.codepolitan.pemasaranproduk.presentation.detailproduct

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.PagerSnapHelper
import com.codepolitan.pemasaranproduk.databinding.ActivityDetailProductBinding

class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding
    private lateinit var productImageAdapter: ProductImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initProductImageAdapter()
    }

    private fun initProductImageAdapter() {
        productImageAdapter = ProductImageAdapter()
        binding.rvImageSlider.adapter = productImageAdapter

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.rvImageSlider)

        binding.ciImageSlider.attachToRecyclerView(binding.rvImageSlider, pagerSnapHelper)
    }
}