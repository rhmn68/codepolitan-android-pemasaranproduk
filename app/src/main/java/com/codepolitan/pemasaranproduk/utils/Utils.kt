package com.codepolitan.pemasaranproduk.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.codepolitan.pemasaranproduk.databinding.*
import com.google.android.gms.maps.model.LatLng
import java.io.FileNotFoundException
import java.io.Serializable
import java.util.*

fun View.visible(){ visibility = View.VISIBLE }
fun View.gone(){ visibility = View.GONE }
fun View.invisible(){ visibility = View.INVISIBLE }
fun View.enabled(){ isEnabled = true }
fun View.disabled(){ isEnabled = false }

inline fun <reified T: Activity> Context.startActivity(vararg params: Pair<String, Any?>){
  val intent = Intent(this, T::class.java)
  if (params.isNotEmpty()) fillIntentArguments(intent, params)
  this.startActivity(intent)
}

fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
  params.forEach {
    when (val value = it.second) {
      null -> intent.putExtra(it.first, null as Serializable?)
      is Int -> intent.putExtra(it.first, value)
      is Long -> intent.putExtra(it.first, value)
      is CharSequence -> intent.putExtra(it.first, value)
      is String -> intent.putExtra(it.first, value)
      is Float -> intent.putExtra(it.first, value)
      is Double -> intent.putExtra(it.first, value)
      is Char -> intent.putExtra(it.first, value)
      is Short -> intent.putExtra(it.first, value)
      is Boolean -> intent.putExtra(it.first, value)
      is Serializable -> intent.putExtra(it.first, value)
      is Bundle -> intent.putExtra(it.first, value)
      is Parcelable -> intent.putExtra(it.first, value)
      is Array<*> -> when {
        value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
        value.isArrayOf<String>() -> intent.putExtra(it.first, value)
        value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
        else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
      }
      is IntArray -> intent.putExtra(it.first, value)
      is LongArray -> intent.putExtra(it.first, value)
      is FloatArray -> intent.putExtra(it.first, value)
      is DoubleArray -> intent.putExtra(it.first, value)
      is CharArray -> intent.putExtra(it.first, value)
      is ShortArray -> intent.putExtra(it.first, value)
      is BooleanArray -> intent.putExtra(it.first, value)
      else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
    }
    return@forEach
  }
}

fun Context.toast(message: CharSequence): Toast = Toast
  .makeText(this, message, Toast.LENGTH_SHORT)
  .apply {
    show()
  }

fun showDialogSuccess(context: Context, message: String): AlertDialog{
  val binding = LayoutDialogSuccessBinding.inflate(LayoutInflater.from(context))
  binding.tvMessage.text = message
  
  return AlertDialog
    .Builder(context)
    .setView(binding.root)
    .setCancelable(true)
    .create()
}

fun showDialogError(context: Context, message: String){
  val binding = LayoutDialogErrorBinding.inflate(LayoutInflater.from(context))
  binding.tvMessage.text = message
  
  AlertDialog
    .Builder(context)
    .setView(binding.root)
    .setCancelable(true)
    .create()
    .show()
}

fun showDialogNotification(context: Context, message: String){
  val binding = LayoutDialogNotificationBinding.inflate(LayoutInflater.from(context))
  binding.tvMessage.text = message
  
  AlertDialog
    .Builder(context)
    .setView(binding.root)
    .setCancelable(true)
    .create()
    .show()
}

fun showDialogLoading(context: Context): AlertDialog{
  val binding = LayoutDialogLoadingBinding.inflate(LayoutInflater.from(context))
  
  return AlertDialog
    .Builder(context)
    .setView(binding.root)
    .setCancelable(true)
    .create()
}

/**
 * Get height screen
 * */
fun Activity.getHeightScreen(): Int{
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    val windowMetrics = windowManager.currentWindowMetrics
    windowMetrics.bounds.height()
  } else {
    val displayMetrics = DisplayMetrics()
    @Suppress("DEPRECATION")
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    displayMetrics.heightPixels
  }
}

fun LatLng.convertToAddress(context: Context): String?{
  val geocode = Geocoder(context, Locale.getDefault())
  val addresses = geocode.getFromLocation(this.latitude, this.longitude, 1)

  if (addresses.size > 0){
    return addresses[0].getAddressLine(0)
  }
  return null
}

fun getRealPath(context: Context, uri: Uri): String {
  var realPath = ""
  try {
    if (uri.scheme.equals("content")){
      val projection = arrayOf("_data")
      val cursor = context.contentResolver.query(uri, projection, null, null, null)
      if (cursor != null){
        val idColumn = cursor.getColumnIndexOrThrow("_data")
        cursor.moveToFirst()
        realPath = cursor.getString(idColumn)
        cursor.close()
      }
    }else if (uri.scheme.equals("file")){
      realPath = uri.path.toString()
    }
  }catch (e: FileNotFoundException){
    e.printStackTrace()
  }
  return realPath
}