package com.codepolitan.pemasaranproduk.presentation.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.codepolitan.pemasaranproduk.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
 
  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!
  private lateinit var recommendProductAdapter: RecommendProductAdapter
  
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
    
    binding.btnCurrentLocation.setOnClickListener {  }
    
    recommendProductAdapter.onClick {
    
    }
  }
  
  private fun initRecommendProduct() {
    recommendProductAdapter = RecommendProductAdapter()
    binding.rvRecommendProductHome.adapter = recommendProductAdapter
  }
}