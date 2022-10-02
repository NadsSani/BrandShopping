package com.nads.shopping.ui.home.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.PagerSnapHelper

import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentFullProductListBinding
import com.nads.shopping.pagingsource.BasicPagingSourceAdapter

import com.nads.shopping.ui.home.product.ProductDetailsDialog
import com.nads.shopping.utils.BEST_SELLER
import com.nads.shopping.utils.BRANDS
import com.nads.shopping.utils.NEW_COLLECTION
import com.nads.shopping.utils.isEnglish
import com.nads.shopping.viewmodels.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FullProductList : Fragment() {
    companion object {
        fun newInstance() =
            FullProductList
    }
    private val brandsViewModel: BrandViewModel by viewModels()
    private val catViewModel: CategoriesViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    private val productViewModel: ProductsViewModel by viewModels()
//    val productViewModel by viewModels<ProductsViewModel> (ownerProducer =
//        {requireParentFragment().requireParentFragment()})
    private lateinit var binding: FragmentFullProductListBinding


    private val args: HomeFragmentArgs by navArgs()
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }
        val viewModel by viewModels<HomeNavigatorViewModel>(ownerProducer = {
            requireParentFragment().requireParentFragment() })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_full_product_list, container, false)
        binding.productViewModel = productViewModel
        binding.catViewModel = catViewModel
        binding.homeViewModel = homeViewModel
        binding.brandViewModel = brandsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        PagerSnapHelper().attachToRecyclerView(binding.rvBanner)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.indicator.attachToRecyclerView(binding.rvBanner)
        }, 2000)

        productViewModel.productSelectEvent.observe(viewLifecycleOwner) {
            ProductDetailsDialog.display(
               childFragmentManager, it.productsId)
        }
        catViewModel.selectedall.observe(viewLifecycleOwner){
            productViewModel.productList.clear()
        }




                binding.btnBestSellerShowMore.setOnClickListener {
            val destination =
                FullProductListDirections.actionFullProductListToProductListFragment(
                    type = BEST_SELLER,
                    title = "Best Seller"
                )
            findNavController().navigate(destination)
        }

        binding.btnNewCollectionShowMore.setOnClickListener {
            val destination =
                FullProductListDirections.actionFullProductListToProductListFragment(
                    type = NEW_COLLECTION,
                    title = "New Collections"
                )
          findNavController().navigate(destination)
        }
        brandsViewModel.brandSelectEvent.observe(viewLifecycleOwner) {
            val destination =
                FullProductListDirections.actionFullProductListToProductListFragment(
                    type = BRANDS,
                    id = it.id,
                    title = if (requireActivity().isEnglish()) it.name else it.nameAr
                )
            findNavController().navigate(destination)
        }


        if (productViewModel.homeBestSellers.isEmpty())
            productViewModel.getBestSellers()

        if (productViewModel.homeNewCollections.isEmpty())
            productViewModel.getNewCollections()

        if (brandsViewModel.brandList.isEmpty())
            brandsViewModel.getBrands()

        if (catViewModel.categories.isEmpty())
            catViewModel.getCategories(true)

//        if (productViewModel.productList.isEmpty())
//            productViewModel.getProducts()
//


        productViewModel.productsId.observe(viewLifecycleOwner){
            ProductDetailsDialog.display(
                childFragmentManager, it.toString())
            Log.e("Clikckedvalue",it.toString())
        };
        val viewAdapter = BasicPagingSourceAdapter(BasicPagingSourceAdapter.UserComparator,this.requireParentFragment().requireActivity(),productViewModel)
        val recyclerfullproduct = binding.recyclerView8
        lifecycleScope.launch {
            productViewModel.getPaginSource().collectLatest {
                viewAdapter.submitData(it)
            }
        }
            recyclerfullproduct.apply {
                adapter = viewAdapter
                isVisible = true
            }


         return binding.root
    }



}
//