package com.codepolitan.pemasaranproduk.presentation.detailproduct

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.PagerSnapHelper
import com.codepolitan.pemasaranproduk.data.model.product.DataProduct
import com.codepolitan.pemasaranproduk.data.model.product.ImageProduct
import com.codepolitan.pemasaranproduk.databinding.ActivityDetailProductBinding
import com.codepolitan.pemasaranproduk.presentation.location.LocationActivity
import com.codepolitan.pemasaranproduk.presentation.resultproduct.ResultProductActivity
import com.codepolitan.pemasaranproduk.utils.convertToAddress
import com.codepolitan.pemasaranproduk.utils.showDialogNotification
import com.codepolitan.pemasaranproduk.utils.startActivity
import com.google.android.gms.maps.model.LatLng

class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding
    private lateinit var productImageAdapter: ProductImageAdapter
    private var location: LatLng? = null

    private val startMapForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data
            val mLocation = data?.getParcelableExtra<LatLng>(LocationActivity.EXTRA_LOCATION)
            if (mLocation != null) {
                location = mLocation
                val address = location?.convertToAddress(this)
                binding.tvCurrentLocation.text = address.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productImageAdapter = ProductImageAdapter()

        getData()
        onAction()
    }

    private fun onAction() {
        binding.btnChat.setOnClickListener {
            val number = "+628999488990"
            val message = "tanya dong kak..."

            val url = "https://api.whatsapp.com/send?phone=$number&text=$message"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.btnChat.setOnClickListener {
            val number = "+628999488990"
            val dialPhoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
            startActivity(dialPhoneIntent)
        }

        binding.btnBackSearch.setOnClickListener { onBackPressed() }

        binding.btnCurrentLocation.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startMapForResult.launch(intent)
        }

        binding.etSearch.setOnEditorActionListener { v, actionId, _ ->
            val title = v.text.toString().trim()

            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                if (title.isEmpty()){
                    showDialogNotification(this, "Please field your search")
                }else{
                    startActivity<ResultProductActivity>(
                        ResultProductActivity.EXTRA_TITLE to title,
                        ResultProductActivity.EXTRA_LOCATION to location
                    )
                }
            }
            return@setOnEditorActionListener false
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun getData() {
        if (intent != null){
            val product = intent.getParcelableExtra<DataProduct>(EXTRA_PRODUCT)

            if (product != null){
                binding.tvPriceProduct.text = product.price.toString()
                binding.tvTitleProduct.text = product.title
                binding.tvLocationProduct.text = product.address
                binding.tvDescriptionProduct.text = product.description
                binding.tvBrandProduct.text = product.brand
                binding.tvModelProduct.text = product.model
                binding.tvYearProduct.text = product.year

                initSliderImage(product)

                val iFrame = "<iframe \n" +
                        "  width=\"100%\" \n" +
                        "  height=\"100%\" \n" +
                        "  frameborder=\"0\" \n" +
                        "  scrolling=\"no\" \n" +
                        "  marginheight=\"0\" \n" +
                        "  marginwidth=\"0\" \n" +
                        "  src=\"https://maps.google.com/maps?q=${product.locLatitude},${product.locLongitude}&z=18&amp;output=embed\"\n" +
                        " ></iframe>"
                binding.wvDetailProduct.settings.javaScriptEnabled = true
                binding.wvDetailProduct.loadData(iFrame, "text/html", "utf-8")
            }
        }
    }

    private fun initSliderImage(product: DataProduct) {
        productImageAdapter.images = product.imageProducts as MutableList<ImageProduct>

        binding.rvImageSlider.adapter = productImageAdapter

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.rvImageSlider)

        binding.ciImageSlider.attachToRecyclerView(binding.rvImageSlider, pagerSnapHelper)
    }

    companion object{
        const val EXTRA_PRODUCT = "extra_product"
    }
}