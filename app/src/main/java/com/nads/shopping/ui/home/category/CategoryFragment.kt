package com.nads.shopping.ui.home.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentCategoriesBinding
import com.nads.shopping.utils.CATEGORY
import com.nads.shopping.viewmodels.BrandViewModel
import com.nads.shopping.viewmodels.CategoriesViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory

class CategoryFragment : Fragment() {

    companion object {
        fun newInstance() =
            CategoryFragment()
    }

    private val viewModel: CategoriesViewModel by viewModels()
    private val brandsViewModel: BrandViewModel by viewModels()
    private lateinit var binding: FragmentCategoriesBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false)
        binding.viewModel = viewModel
        binding.brandsViewModel = brandsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeActivityViewModel.showTitle.value = true
        homeActivityViewModel.showSearchBar.value = false
        homeActivityViewModel.showBack.value = true
        homeActivityViewModel.showSearchIcon.value = true

        homeActivityViewModel.title.value = getString(R.string.categories)

        viewModel.subCategorySelectEvent.observe(viewLifecycleOwner) {
            val destination =
                CategoryFragmentDirections.actionNavCategoriesToNavProductList(
                    type = CATEGORY,
                    id = it.id,
                    title = it.name
                )
            findNavController().navigate(destination)
        }

        if (brandsViewModel.brandList.isEmpty())
            brandsViewModel.getBrands()

        if (viewModel.categories.isEmpty())
            viewModel.getCategories(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}