package com.codepolitan.pemasaranproduk.presentation.resultproduct

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.codepolitan.pemasaranproduk.data.model.Resource
import com.codepolitan.pemasaranproduk.databinding.ActivityResultProductBinding
import com.codepolitan.pemasaranproduk.presentation.detailproduct.DetailProductActivity
import com.codepolitan.pemasaranproduk.utils.showDialogError
import com.codepolitan.pemasaranproduk.utils.startActivity
import com.google.android.gms.maps.model.LatLng

class ResultProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultProductBinding
    private lateinit var resultProductAdapter: ResultProductAdapter
    private lateinit var resultProductViewModel: ResultProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Init
        resultProductViewModel = ViewModelProvider(this).get(ResultProductViewModel::class.java)

        initResultProductAdapter()
        getDataIntent()
        onAction()
    }

    private fun onAction() {
        binding.btnBackSearch.setOnClickListener { onBackPressed() }

        resultProductAdapter.onClick { dataProduct ->
            startActivity<DetailProductActivity>(
                DetailProductActivity.EXTRA_PRODUCT to dataProduct
            )
        }
    }

    private fun getDataIntent() {
        if (intent != null){
            val title = intent.getStringExtra(EXTRA_TITLE)
            val location = intent.getParcelableExtra<LatLng>(EXTRA_LOCATION)

            getDataProduct(title, location)
        }
    }

    private fun getDataProduct(title: String?, location: LatLng?) {
        resultProductViewModel
            .findAds(location?.latitude!!, location.longitude, title.toString())
            .observe(this){ state ->
                when(state){
                    Resource.Empty -> {
                        binding.swipeResultProduct.isRefreshing = false
                    }
                    is Resource.Error -> {
                        binding.swipeResultProduct.isRefreshing = false
                        val errorMessage = state.errorMessage
                        showDialogError(this, errorMessage)
                    }
                    Resource.Loading -> {
                        binding.swipeResultProduct.isRefreshing = true
                    }
                    is Resource.Success -> {
                        val data = state.data
                        resultProductAdapter.differ.submitList(data.dataProduct)

                        binding.swipeResultProduct.isRefreshing = false
                    }
                }
            }
    }

    private fun initResultProductAdapter() {
        resultProductAdapter = ResultProductAdapter()
        binding.rvProductResult.adapter = resultProductAdapter
    }

    companion object{
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_LOCATION = "extra_location"
    }
}