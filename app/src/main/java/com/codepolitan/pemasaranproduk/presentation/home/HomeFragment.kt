package com.codepolitan.pemasaranproduk.presentation.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.codepolitan.pemasaranproduk.databinding.FragmentHomeBinding
import com.codepolitan.pemasaranproduk.presentation.detailproduct.DetailProductActivity
import com.codepolitan.pemasaranproduk.presentation.location.LocationActivity
import com.codepolitan.pemasaranproduk.presentation.main.MainActivity
import com.codepolitan.pemasaranproduk.utils.convertToAddress
import com.codepolitan.pemasaranproduk.utils.startActivity
import com.google.android.gms.maps.model.LatLng

class HomeFragment : Fragment() {
 
  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!
  private lateinit var recommendProductAdapter: RecommendProductAdapter
  private var location: LatLng? = null

  private val startMapResult = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ){
    if (it.resultCode == Activity.RESULT_OK){
      val data = it.data
      val mLocation = data?.getParcelableExtra<LatLng>(LocationActivity.EXTRA_LOCATION)
      if (mLocation != null){
        location = mLocation
        val address = location?.convertToAddress(requireContext())
        binding.tvCurrentLocation.text = address.toString()
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return _binding?.root
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    initRecommendProduct()
    onAction()
  }
  
  private fun onAction() {
    binding.btnSearchHome.setOnClickListener {  }
    
    binding.btnCurrentLocation.setOnClickListener {
      val intent = Intent(context, LocationActivity::class.java)
      startMapResult.launch(intent)
    }

    recommendProductAdapter.onClick {
      (activity as MainActivity).startActivity<DetailProductActivity>()
    }
  }
  
  private fun initRecommendProduct() {
    recommendProductAdapter = RecommendProductAdapter()
    binding.rvRecommendProductHome.adapter = recommendProductAdapter
  }
}