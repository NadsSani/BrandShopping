package com.nads.shopping.ui.home.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentProductListBinding
import com.nads.shopping.datamodels.FilterType
import com.nads.shopping.pagingsource.BasicPagingSourceAdapter
import com.nads.shopping.utils.BEST_SELLER
import com.nads.shopping.utils.BRANDS
import com.nads.shopping.utils.NEW_COLLECTION
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.HomeNavigatorViewModel
import com.nads.shopping.viewmodels.ProductsViewModel
import com.nads.shopping.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ProductListFragment : Fragment() {

    companion object {
        fun newInstance() =
            ProductListFragment()
    }
    val viewModeler by viewModels<HomeNavigatorViewModel>(ownerProducer = {
        requireParentFragment().requireParentFragment() })
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var binding: FragmentProductListBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }
    private val args: ProductListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeActivityViewModel.showTitle.value = false
        homeActivityViewModel.showSearchBar.value = true
        homeActivityViewModel.showBack.value = true
        homeActivityViewModel.showSearchIcon.value = false
        if (args.type == BEST_SELLER || args.type == NEW_COLLECTION || args.type == BRANDS){
            viewModeler.type.value = args.type
            viewModeler.title.value = args.title
            viewModeler.id.value = args.id

        }
        viewModel.productListTitle.value = viewModeler.title.value

        viewModel.productSelectEvent.observe(viewLifecycleOwner) {
            ProductDetailsDialog.display(childFragmentManager, it.productsId)
        }

//        binding.layoutFilter.setOnClickListener {
//            FilterBottomSheet().show(childFragmentManager, "filter")
//        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            findNavController().navigateUp()
        }

        homeActivityViewModel.filterSelectEvent.observe(requireActivity()) {
            var categories = ""
            var brands = ""
            var price = if (homeActivityViewModel.isPriceLH.value==true) "D_A" else "A_D"

            if (it)
            homeActivityViewModel.selectedFilters.forEachIndexed { index, filter ->
                when (filter.type) {
                    FilterType.CATEGORY -> {
                        categories += filter.id+","
                    }
                    FilterType.BRANDS -> {
                        brands += filter.id+","
                    }
                }
            }

            viewModel.getProducts(categoryId = categories, brandId = brands, priceFilter = price)
        }
        val viewAdapter = BasicPagingSourceAdapter(BasicPagingSourceAdapter.UserComparator,this.requireParentFragment().requireActivity(),viewModel)
        val recyclernewproduct = binding.recyclerView2
        viewModel.productsId.observe(viewLifecycleOwner){
            ProductDetailsDialog.display(
                childFragmentManager, it.toString())
            Log.e("Clikckedvalue",it.toString())
        };


        when (viewModeler.type.value) {
            BEST_SELLER -> bestSellerPaging(viewAdapter,recyclernewproduct)

            NEW_COLLECTION ->  newCollectionPaging(viewAdapter,recyclernewproduct)

            BRANDS -> viewModel.getBrandProducts(brandId = viewModeler.id.value)


            else -> categoryProductPaging(viewAdapter,recyclernewproduct)
        }


        return binding.root
    }

    fun bestSellerPaging(viewAdapter: BasicPagingSourceAdapter,recyclerView: RecyclerView){

        lifecycleScope.launch {
            viewModel.getBestPaginSource().collectLatest {
                viewAdapter.submitData(it)
            }
        }
        recyclerView.apply {
            adapter = viewAdapter
            isVisible = true
        }
    }

    fun categoryProductPaging(viewAdapter: BasicPagingSourceAdapter,recyclerView: RecyclerView){
        lifecycleScope.launch {
            viewModeler.id.value?.let {
                viewModel.getPaginSource( subCategoryId = it).collectLatest {
                    viewAdapter.submitData(it)
                }
            }
        }
        recyclerView.apply {
            adapter = viewAdapter
            isVisible = true
        }
    }


     fun newCollectionPaging(viewAdapter:BasicPagingSourceAdapter,recyclerView: RecyclerView){

         lifecycleScope.launch {
             viewModel.getNewCollectionPaginSource().collectLatest {
                 viewAdapter.submitData(it)
             }
         }
         recyclerView.apply {
             adapter = viewAdapter
             isVisible = true
         }


        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

         }

}