package com.codepolitan.pemasaranproduk.presentation.resultproduct

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.codepolitan.pemasaranproduk.data.model.Resource
import com.codepolitan.pemasaranproduk.databinding.ActivityResultProductBinding
import com.codepolitan.pemasaranproduk.presentation.detailproduct.DetailProductActivity
import com.codepolitan.pemasaranproduk.presentation.location.LocationActivity
import com.codepolitan.pemasaranproduk.utils.*
import com.google.android.gms.maps.model.LatLng

class ResultProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultProductBinding
    private lateinit var resultProductAdapter: ResultProductAdapter
    private lateinit var resultProductViewModel: ResultProductViewModel
    private var location: LatLng? = null

    private val startMapResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            val data = it.data
            val mLocation = data?.getParcelableExtra<LatLng>(LocationActivity.EXTRA_LOCATION)
            if (mLocation != null){
                location = mLocation
                val address = location?.convertToAddress(this)
                binding.tvCurrentLocation.text = address.toString()
            }
        }
    }

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

        binding.swipeResultProduct.setOnRefreshListener {
            getDataIntent()
        }

        binding.btnCurrentLocation.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startMapResult.launch(intent)
        }

        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->
            val title = textView.text.toString().trim()

            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                if (title.isEmpty()){
                    showDialogNotification(this, "Please field your search")
                }else{
                    getDataProduct(title, location, true)
                }
            }
            return@setOnEditorActionListener false
        }

    }

    private fun getDataIntent() {
        if (intent != null){
            val title = intent.getStringExtra(EXTRA_TITLE)
            val location = intent.getParcelableExtra<LatLng>(EXTRA_LOCATION)

            getDataProduct(title, location, false)
        }
    }

    private fun getDataProduct(title: String?, location: LatLng?, isNew: Boolean) {
        resultProductViewModel
            .findAds(location?.latitude!!, location.longitude, title.toString(), isNew)
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
                        Log.d("coba", "getDataProduct: $data")
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