package com.codepolitan.pemasaranproduk.presentation.uploadphoto

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.codepolitan.pemasaranproduk.data.hawkstorage.HawkStorage
import com.codepolitan.pemasaranproduk.data.model.Resource
import com.codepolitan.pemasaranproduk.data.model.product.DataProduct
import com.codepolitan.pemasaranproduk.data.model.product.ImageProduct
import com.codepolitan.pemasaranproduk.databinding.ActivityUploadPhotoBinding
import com.codepolitan.pemasaranproduk.presentation.main.MainActivity
import com.codepolitan.pemasaranproduk.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileNotFoundException

class UploadPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadPhotoBinding
    private lateinit var uploadPhotoAdapter: UploadPhotoAdapter
    private var position = 0
    private lateinit var uploadPhotoViewModel: UploadPhotoViewModel
    private lateinit var dialogLoading: AlertDialog
    private lateinit var dataProduct: DataProduct
    private lateinit var token: String
    private var isEdit = false

    private val startPhotoResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            try {
                val imageUri = it.data?.data
                if (imageUri != null){
                    val path = getRealPath(this, imageUri)
                    val imageStream = contentResolver.openInputStream(imageUri)
                    //Convert dari uri ke bitmap
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    val imageProduct = ImageProduct(preview = selectedImage, path = path, uri = imageUri)
                    uploadPhotoAdapter.updateItem(imageProduct, position)
                }
            }catch (e: FileNotFoundException){
                e.printStackTrace()
            }
        }
    }

    private val storagePermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uploadPhotoViewModel = ViewModelProvider(this).get(UploadPhotoViewModel::class.java)
        dialogLoading = showDialogLoading(this)

        getDataToken()
        getDataIntent()
        initActionBar()
        initUploadPhotoAdapter()
        onAction()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbUploadPhoto)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getDataIntent() {
        if (intent != null){
            val ads = intent.getParcelableExtra<DataProduct>(EXTRA_ADS)
            isEdit = intent.getBooleanExtra(EXTRA_IS_EDIT, false)
            if (ads != null){
                dataProduct = ads
            }
        }
    }

    private fun getDataToken() {
        val user = HawkStorage.instance(this).getUser()
        token = user.accessToken
    }

    private fun onAction() {
        binding.btnUploadPhoto.setOnClickListener {
            val images = uploadPhotoAdapter.images
            if (checkValid(images)){
                uploadPhotoToServer(images)
            }
        }

        binding.tbUploadPhoto.setNavigationOnClickListener {
            startActivity<MainActivity>()
            finishAffinity()
        }

        uploadPhotoAdapter.onClick {
            requestOpenStorage()
            position = it
        }

        uploadPhotoAdapter.onClickRemove { imageProduct, position ->
            if (imageProduct.preview == null){
                deleteImageToServer(imageProduct, position)
            }else{
                uploadPhotoAdapter.remove(position)
            }
        }
    }

    private fun uploadPhotoToServer(images: java.util.ArrayList<ImageProduct?>) {
        val imagesParts = mutableListOf<MultipartBody.Part>()

        for (image in images){
            val path = image?.path
            val uri = image?.uri
            if (uri != null && path != null){
                val file = File(path)
                val typeFile = contentResolver.getType(uri)
                val mediaTypeFile= typeFile?.toMediaType()
                val requestPhotoFile = file.asRequestBody(mediaTypeFile)
                val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestPhotoFile)
                imagesParts.add(multipartBody)
            }
        }

        val id = dataProduct.id

        uploadPhotoViewModel.uploadImages(token, id!!, imagesParts).observe(this){ state->
            when(state){
                Resource.Empty -> {
                    dialogLoading.dismiss()
                    showDialogNotification(this, "EMPTY")
                }
                is Resource.Error -> {
                    dialogLoading.dismiss()
                    val errorMessage = state.errorMessage
                    showDialogError(this, errorMessage)
                }
                Resource.Loading -> {
                    dialogLoading.show()
                }
                is Resource.Success -> {
                    dialogLoading.dismiss()
                    val data = state.data
                    val dialogSuccess = showDialogSuccess(this, data.message.toString())
                    dialogSuccess.show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        dialogSuccess.dismiss()
                        startActivity<MainActivity>()
                        finishAffinity()
                    }, 1200)
                }
            }
        }
    }

    private fun checkValid(images: java.util.ArrayList<ImageProduct?>): Boolean {
        var isValid = false
        for (image in images){
            if (image?.path != null || image?.image != null){
                isValid = true
                break
            }else{
                isValid = false
                showDialogError(this, "Please select your photo")
                break
            }
        }
        return isValid
    }

    private fun deleteImageToServer(imageProduct: ImageProduct, position: Int) {
        if (imageProduct.id != null){
            uploadPhotoViewModel.deleteImage(token, imageProduct.id).observe(this){state->
                when(state){
                    Resource.Empty -> {
                        dialogLoading.dismiss()
                        showDialogNotification(this, "EMPTY")
                    }
                    is Resource.Error -> {
                        dialogLoading.dismiss()
                        val errorMessage = state.errorMessage
                        showDialogError(this, errorMessage)
                    }
                    Resource.Loading -> {
                        dialogLoading.show()
                    }
                    is Resource.Success -> {
                        dialogLoading.dismiss()
                        uploadPhotoAdapter.remove(position)
                    }
                }
            }
        }
    }

    private fun requestOpenStorage() {
       if (checkPermissionStorage()){
           openStorage()
       }else{
           requestPermissionStorage()
       }
    }

    private fun requestPermissionStorage() {
        requestPermissions(storagePermissions, REQUEST_CODE_STORAGE_PERMISSION)
    }

    private fun openStorage() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startPhotoResult.launch(photoPickerIntent)
    }

    private fun checkPermissionStorage(): Boolean {
        var isHasPermission = false
        for (permission in storagePermissions){
            isHasPermission = ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
        return isHasPermission
    }

    private fun initUploadPhotoAdapter() {
        uploadPhotoAdapter = UploadPhotoAdapter()
        val images = mutableListOf<ImageProduct?>()
        val dataImages = dataProduct.imageProducts
        if (dataImages != null && dataImages.isNotEmpty()){
            images.addAll(dataImages)
        }
        if (images.size < 8){
            val total = 8 - images.size
            for (i in 0 until total){
                images.add(null)
            }
        }
        uploadPhotoAdapter.images = images as ArrayList<ImageProduct?>

        binding.rvUploadPhoto.adapter = uploadPhotoAdapter
    }

    companion object{
        const val EXTRA_ADS = "extra_ads"
        const val EXTRA_IS_EDIT = "extra_is_edit"
        const val REQUEST_CODE_STORAGE_PERMISSION = 1000
    }
}