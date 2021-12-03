package com.codepolitan.pemasaranproduk.presentation.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.codepolitan.pemasaranproduk.data.hawkstorage.HawkStorage
import com.codepolitan.pemasaranproduk.data.model.Resource
import com.codepolitan.pemasaranproduk.data.model.product.ProductResponse
import com.codepolitan.pemasaranproduk.databinding.FragmentHomeBinding
import com.codepolitan.pemasaranproduk.presentation.detailproduct.DetailProductActivity
import com.codepolitan.pemasaranproduk.presentation.location.LocationActivity
import com.codepolitan.pemasaranproduk.presentation.login.LoginActivity
import com.codepolitan.pemasaranproduk.presentation.main.MainActivity
import com.codepolitan.pemasaranproduk.presentation.resultproduct.ResultProductActivity
import com.codepolitan.pemasaranproduk.utils.*
import com.google.android.gms.maps.model.LatLng

class HomeFragment : Fragment() {
 
  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!
  private lateinit var recommendProductAdapter: RecommendProductAdapter
  private var location: LatLng? = null
  private var productResponse: ProductResponse? = null
  private var isFirstPage = true
  private lateinit var homeViewModel: HomeViewModel

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

  private val onScrollState = NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
    val childHeight = v.getChildAt(0).measuredHeight
    val height = v.measuredHeight
    val totalHeight = childHeight - height
    val isScroll = productResponse?.currentPage!! < productResponse?.totalPages!!
    if (scrollY == totalHeight && isScroll){
      getDataProduct(false)
    }

    if (!isScroll){
      binding.pbLoadMore.gone()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    recommendProductAdapter = RecommendProductAdapter()
    homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
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
    getDataProduct(true)
  }

  @SuppressLint("NotifyDataSetChanged")
  private fun getDataProduct(isSwipe: Boolean) {
    val token = HawkStorage.instance(context).getUser().accessToken

    homeViewModel.getDataProducts(token, isSwipe).observe(viewLifecycleOwner){ state->
      when(state){
        Resource.Empty -> {
          binding.swipeHome.isRefreshing = false
          binding.pbLoadMore.gone()
          showEmpty()
        }
        is Resource.Error -> {
          hideEmptyData()
          binding.swipeHome.isRefreshing = false
          binding.pbLoadMore.gone()

          val errorMessage = state.errorMessage
          if (errorMessage.lowercase().trim() == "unauthorized!"){
            context?.startActivity<LoginActivity>()
            (activity as MainActivity).finishAffinity()
          }else{
            showDialogError(requireContext(), errorMessage)
          }
        }
        Resource.Loading -> {
          if (isFirstPage){
            binding.swipeHome.isRefreshing = true
          }else{
            binding.pbLoadMore.visible()
          }
        }
        is Resource.Success ->{
          hideEmptyData()
          binding.swipeHome.isRefreshing = false
          binding.pbLoadMore.invisible()

          isFirstPage = false

          val data = state.data
          val dataProducts = data.dataProduct
          productResponse = data

          //TODO Tampilkan di adapter
          recommendProductAdapter.differ.submitList(dataProducts)
          recommendProductAdapter.notifyDataSetChanged()
        }
      }
    }
  }

  private fun hideEmptyData() {
    binding.ivEmpty.gone()
    binding.pbLoadMore.gone()
    binding.rvRecommendProductHome.visible()
  }

  private fun showEmpty() {
    binding.ivEmpty.visible()
    binding.pbLoadMore.gone()
    binding.rvRecommendProductHome.gone()
  }

  private fun onAction() {
    binding.etSearchHome.setOnEditorActionListener { textView, actionId, _ ->
      val title = textView.toString().trim()

      if (actionId == EditorInfo.IME_ACTION_SEARCH){
        if (title.isEmpty()){
          showDialogNotification(requireContext(), "Please field your search")
        }else{
          (activity as MainActivity).startActivity<ResultProductActivity>(
            ResultProductActivity.EXTRA_TITLE to title,
            ResultProductActivity.EXTRA_LOCATION to location
          )
        }
      }
      return@setOnEditorActionListener false
    }
    
    binding.btnCurrentLocation.setOnClickListener {
      val intent = Intent(context, LocationActivity::class.java)
      startMapResult.launch(intent)
    }

    recommendProductAdapter.onClick {
      (activity as MainActivity).startActivity<DetailProductActivity>(
        DetailProductActivity.EXTRA_PRODUCT to it
      )
    }
  }
  
  private fun initRecommendProduct() {
    binding.rvRecommendProductHome.adapter = recommendProductAdapter
    binding.nestedHome.setOnScrollChangeListener(onScrollState)
  }
}