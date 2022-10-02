package com.nads.shopping.ui.home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentHomeBinding
import com.nads.shopping.utils.*
import com.nads.shopping.viewmodels.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() =
            HomeFragment()
    }
    private lateinit var localController: NavController
    private val brandsViewModel: BrandViewModel by viewModels()
    private val catViewModel: CategoriesViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    private val productViewModel: ProductsViewModel by viewModels()
    private val homeNavigatorViewModel: HomeNavigatorViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    private val args: HomeFragmentArgs by navArgs()

    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.productViewModel = productViewModel
        binding.catViewModel = catViewModel
        binding.homeViewModel = homeViewModel
        binding.brandViewModel = brandsViewModel
        binding.homenavigatorviewmodel = homeNavigatorViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val localNavHost = childFragmentManager.findFragmentById(R.id.productlistnavigation) as NavHostFragment
        localController = localNavHost.navController
        homeActivityViewModel.showTitle.value = false
        homeActivityViewModel.showSearchBar.value = true
        homeActivityViewModel.showBack.value = false
        homeActivityViewModel.showSearchIcon.value = false
        catViewModel.selectedCat = args.selectedcat
//        val viewModel by viewModels<HomeNavigatorViewModel>(ownerProducer = {
//            requireParentFragment().requireParentFragment() })
      //for bottombarand appbar
        initialized()
        if (args.message != "0") {
            homeViewModel.showMessage(args.message)
        }
       catViewModel.dressbus.observe(viewLifecycleOwner){
           findNavController().navigate(R.id.dressBus)
       }
        catViewModel.toysbus.observe(viewLifecycleOwner){
            findNavController().navigate(R.id.toysBus)
        }
        catViewModel.allisactive.observe(viewLifecycleOwner){
//            homeNavigatorViewModel.type.value = CATEGORY
//            homeNavigatorViewModel.id.value = it.id
//            homeNavigatorViewModel.title.value = it.name
            catViewModel.subCategories.clear()
            localController.navigate(R.id.fullProductList)
        }
//Commentedforediting
//        PagerSnapHelper().attachToRecyclerView(binding.rvBanner)
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            binding.indicator.attachToRecyclerView(binding.rvBanner)
//        }, 2000)
//Commentedforediting

//edited now





//        productViewModel.productSelectEvent.observe(viewLifecycleOwner) {
//            ProductDetailsDialog.display(childFragmentManager, it.productsId)
//        }

//        catViewModel.selectedall.observe(viewLifecycleOwner){
//            productViewModel.productList.clear()
//        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
           findNavController().navigateUp()
        }
//child categories
        catViewModel.subCategorySelectEvent.observe(viewLifecycleOwner) {
//            val destination =
//                HomeFragmentDirections.actionNavHomeToNavProductList(
//                    type = CATEGORY,
//                    id = it.id,
//                    title = it.name
//                )

           homeNavigatorViewModel.type.value = CATEGORY
            homeNavigatorViewModel.id.value = it.id
            homeNavigatorViewModel.title.value = it.name
           localController.navigate(R.id.productListFragment)


//Commentedforediting
//            binding.recyclerView26.isVisible = true
//            productViewModel.getProducts2(subCategoryId = it.id)
//Commentedforediting
        }
//Commentedforediting
//        binding.btnBestSellerShowMore.setOnClickListener {
//            val destination =
//                HomeFragmentDirections.actionNavHomeToNavProductList(
//                    type = BEST_SELLER,
//                    title = "Best Seller"
//                )
//            findNavController().navigate(destination)
//        }
//
//        binding.btnNewCollectionShowMore.setOnClickListener {
//            val destination =
//                HomeFragmentDirections.actionNavHomeToNavProductList(
//                    type = NEW_COLLECTION,
//                    title = "New Collections"
//                )
//            findNavController().navigate(destination)
//        }
//Commentedforediting
        brandsViewModel.brandSelectEvent.observe(viewLifecycleOwner) {
            val destination =
                HomeFragmentDirections.actionNavHomeToNavProductList(
                    type = BRANDS,
                    id = it.id,
                    title = if (requireActivity().isEnglish()) it.name else it.nameAr
                )
            findNavController().navigate(destination)
        }

//        if (productViewModel.homeBestSellers.isEmpty())
//            productViewModel.getBestSellers()
//
//        if (productViewModel.homeNewCollections.isEmpty())
//            productViewModel.getNewCollections()
//
//        if (brandsViewModel.brandList.isEmpty())
//            brandsViewModel.getBrands()

        if (catViewModel.categories.isEmpty())
            catViewModel.getCategories(true)
//        if (productViewModel.productList.isEmpty())
//            productViewModel.getProducts()

        return binding.root
    }



    fun initialized(){
        val appbar = requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout)
        appbar.isVisible = true
        val bottomnavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomnavigation.isVisible =true

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}