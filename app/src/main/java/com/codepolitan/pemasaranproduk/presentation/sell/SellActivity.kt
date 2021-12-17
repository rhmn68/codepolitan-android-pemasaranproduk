package com.codepolitan.pemasaranproduk.presentation.sell

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.codepolitan.pemasaranproduk.R
import com.codepolitan.pemasaranproduk.data.hawkstorage.HawkStorage
import com.codepolitan.pemasaranproduk.data.model.Resource
import com.codepolitan.pemasaranproduk.data.model.product.CreateAdsRequest
import com.codepolitan.pemasaranproduk.data.model.product.DataProduct
import com.codepolitan.pemasaranproduk.databinding.ActivitySellBinding
import com.codepolitan.pemasaranproduk.presentation.location.LocationActivity
import com.codepolitan.pemasaranproduk.presentation.uploadphoto.UploadPhotoActivity
import com.codepolitan.pemasaranproduk.utils.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class SellActivity : AppCompatActivity() {
  
  private lateinit var binding: ActivitySellBinding
  private var location: LatLng? = null
  private lateinit var dialogLoading: AlertDialog
  private lateinit var sellViewModel: SellViewModel
  private var isEdit = false
  private lateinit var token: String
  private var idProduct = 0

  private val startMapResult = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ){
    if (it.resultCode == Activity.RESULT_OK){
      val data = it.data
      val mLocation = data?.getParcelableExtra<LatLng>(LocationActivity.EXTRA_LOCATION)
      if (mLocation != null){
        location = mLocation
        val address = location?.convertToAddress(this)
        binding.etAddressSell.setText(address.toString())
      }
    }
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySellBinding.inflate(layoutInflater)
    setContentView(binding.root)

    dialogLoading = showDialogLoading(this)
    sellViewModel = ViewModelProvider(this).get(SellViewModel::class.java)

    getToken()
    getDataIntent()
    onAction()
  }

  private fun getDataIntent() {
    if (intent != null){
      isEdit = intent.getBooleanExtra(EXTRA_IS_EDIT, false)
      val dataProduct = intent.getParcelableExtra<DataProduct>(EXTRA_DATA_PRODUCT)

      Log.d("coba", "onAction: $isEdit")
      if (dataProduct != null){
        idProduct = dataProduct.id!!
        showDataProduct(dataProduct)
      }
    }
  }

  private fun showDataProduct(dataProduct: DataProduct) {
    binding.etTitleProductSell.setText(dataProduct.title)
    binding.etBrandSell.setText(dataProduct.brand)
    binding.etModelProductSell.setText(dataProduct.model)
    binding.etYearProductSell.setText(dataProduct.year)
    binding.etPriceProductSell.setText(dataProduct.price.toString())
    binding.etAddressSell.setText(dataProduct.address)
    binding.etDescProductSell.setText(dataProduct.description)
    binding.btnSubmitSell.text = getString(R.string.update)

    if (dataProduct.condition == true){
      binding.rbNewSell.isChecked = true
    }else{
      binding.rbSecondHandSell.isChecked = true
    }

    location = LatLng(
      dataProduct.locLatitude!!.toDouble(),
      dataProduct.locLongitude!!.toDouble()
    )
  }

  private fun getToken() {
    val user = HawkStorage.instance(this).getUser()
    token = user.accessToken
  }

  private fun onAction() {
    binding.btnMapSell.setOnClickListener {
      val intent = Intent(this, LocationActivity::class.java)
      startMapResult.launch(intent)
    }

    binding.btnSubmitSell.setOnClickListener {
      val title = binding.etTitleProductSell.text.toString().trim()
      val brand = binding.etBrandSell.text.toString().trim()
      val model = binding.etModelProductSell.text.toString().trim()
      val yearsProduction = binding.etYearProductSell.text.toString().trim()
      val price = binding.etPriceProductSell.text.toString().trim()
      val address = binding.etAddressSell.text.toString().trim()
      val desc = binding.etDescProductSell.text.toString().trim()
      val location = location
      var isNew = false

      val selectId = binding.rgConditionSell.checkedRadioButtonId
      if (selectId == R.id.rb_new_sell){
        isNew = true
      }

      if (checkValid(title, brand, model, yearsProduction, price, location, address, desc)){
        if (isEdit){
          updateAdsToServer(
            title,
            brand,
            model,
            yearsProduction,
            price,
            location,
            isNew,
            address,
            desc
          )
        }else{
          createAdsToServer(
            title,
            brand,
            model,
            yearsProduction,
            price,
            location,
            isNew,
            address,
            desc
          )
        }
      }
    }

    binding.tbSell.setNavigationOnClickListener { onBackPressed() }
  }

  private fun createAdsToServer(
    title: String,
    brand: String,
    model: String,
    yearsProduction: String,
    price: String,
    location: LatLng?,
    isNew: Boolean,
    address: String,
    desc: String
  ) {
    val createAdsRequest = CreateAdsRequest(
      title = title,
      brand = brand,
      model = model,
      year = yearsProduction,
      condition = isNew,
      price = price.toInt(),
      address = address,
      locLatitude = location?.latitude,
      locLongitude = location?.longitude,
      categoryId = 3,
      description = desc,
      sold = false
    )

    val body = Gson().toJson(createAdsRequest)

    sellViewModel.createAds(token, body).observe(this){ state ->
      when(state){
        Resource.Empty -> {
          hideLoading()
          showDialogNotification(this, "EMPTY")
        }
        is Resource.Error -> {
          hideLoading()
          val errorMessage = state.errorMessage
          showDialogError(this, errorMessage)
        }
        Resource.Loading -> {
          showLoading()
        }
        is Resource.Success -> {
          hideLoading()
          val data = state.data
          val dialogSuccess = showDialogSuccess(this, data.message.toString())
          dialogSuccess.setCancelable(true)
          dialogSuccess.show()

          Handler(Looper.getMainLooper()).postDelayed({
            dialogSuccess.dismiss()
            startActivity<UploadPhotoActivity>(
              UploadPhotoActivity.EXTRA_ADS to data.dataCreateAds,
              UploadPhotoActivity.EXTRA_IS_EDIT to false
            )
          }, 1200)
        }
      }
    }
  }

  private fun showLoading() {
    dialogLoading.show()
  }

  private fun hideLoading() {
    dialogLoading.dismiss()
  }

  private fun updateAdsToServer(
    title: String,
    brand: String,
    model: String,
    yearsProduction: String,
    price: String,
    location: LatLng?,
    isNew: Boolean,
    address: String,
    desc: String
  ) {
    val createAdsRequest = CreateAdsRequest(
      title = title,
      brand = brand,
      model = model,
      year = yearsProduction,
      condition = isNew,
      price = price.toInt(),
      address = address,
      locLatitude = location?.latitude,
      locLongitude = location?.longitude,
      categoryId = 3,
      description = desc,
      sold = false
    )

    val body = Gson().toJson(createAdsRequest)

    sellViewModel.updateAds(token, body, idProduct).observe(this){state ->
      when(state){
        Resource.Empty -> {
          hideLoading()
          showDialogNotification(this, "EMPTY")
        }
        is Resource.Error -> {
          hideLoading()
          val errorMessage = state.errorMessage
          showDialogError(this, errorMessage)
        }
        Resource.Loading -> {
          showLoading()
        }
        is Resource.Success -> {
          hideLoading()
          val data = state.data
          val dialogSuccess = showDialogSuccess(this, data.message.toString())
          dialogSuccess.show()

          Handler(Looper.getMainLooper()).postDelayed({
            dialogSuccess.dismiss()
            finish()
          }, 2000)
        }
      }
    }
  }

  private fun checkValid(
    title: String,
    brand: String,
    model: String,
    yearsProduction: String,
    price: String,
    location: LatLng?,
    address: String,
    desc: String
  ): Boolean {
    return when{
      title.isEmpty() -> {
        binding.textInputTitleProductSell.error = getString(R.string.field_title)
        binding.textInputTitleProductSell.requestFocus()
        false
      }
      desc.isEmpty() -> {
        binding.textInputDescProductSell.error = getString(R.string.field_desc)
        binding.textInputDescProductSell.requestFocus()
        false
      }
      brand.isEmpty() -> {
        binding.textInputTitleProductSell.error = null

        binding.textInputBrandSell.error = getString(R.string.field_brand)
        binding.textInputBrandSell.requestFocus()
        false
      }
      model.isEmpty() -> {
        binding.textInputBrandSell.error = null

        binding.textInputModelProductSell.error = getString(R.string.field_model)
        binding.textInputModelProductSell.requestFocus()
        false
      }
      yearsProduction.isEmpty() -> {
        binding.textInputModelProductSell.error = null

        binding.textInputYearProductSell.error = getString(R.string.field_year_production)
        binding.textInputYearProductSell.requestFocus()
        false
      }
      price.isEmpty() -> {
        binding.textInputYearProductSell.error = null

        binding.textInputPriceProductSell.error = getString(R.string.field_price)
        binding.textInputPriceProductSell.requestFocus()
        false
      }
      location == null -> {
        binding.textInputPriceProductSell.error = null

        showDialogError(this, getString(R.string.field_location))
        false
      }
      address.isEmpty() -> {
        binding.textInputAddressSell.error = getString(R.string.field_address)
        binding.textInputAddressSell.requestFocus()
        false
      }
      else -> {
        binding.textInputTitleProductSell.error = null
        binding.textInputBrandSell.error = null
        binding.textInputModelProductSell.error = null
        binding.textInputYearProductSell.error = null
        binding.textInputPriceProductSell.error = null
        binding.textInputAddressSell.error = null
        true
      }
    }
  }

  companion object{
    const val EXTRA_IS_EDIT = "extra_is_edit"
    const val EXTRA_DATA_PRODUCT = "extra_data_product"
  }
}