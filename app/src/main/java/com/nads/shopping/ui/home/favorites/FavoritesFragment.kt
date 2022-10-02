package com.nads.shopping.ui.home.favorites

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentFavoritesBinding
import com.nads.shopping.ui.activities.LoginActivity
import com.nads.shopping.ui.home.product.ProductDetailsDialog
import com.nads.shopping.utils.IKEY_LOGIN
import com.nads.shopping.utils.SIGN_IN_REQUEST_CODE
import com.nads.shopping.utils.getToken
import com.nads.shopping.viewmodels.FavoritesViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() =
            FavoritesFragment()
    }

    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var binding: FragmentFavoritesBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeActivityViewModel.showTitle.value = true
        homeActivityViewModel.showSearchBar.value = false
        homeActivityViewModel.showBack.value = true
        homeActivityViewModel.showSearchIcon.value = true

        homeActivityViewModel.title.value = getString(R.string.favorites)

        if (requireActivity().getToken().isNullOrEmpty())
            binding.layoutSignIn.visibility = View.VISIBLE
        else {
            binding.layoutSignIn.visibility = View.GONE
            viewModel.getFavorites(requireActivity().getToken())
        }

        viewModel.favoritesItemSelectEvent.observe(viewLifecycleOwner){
            ProductDetailsDialog.display(childFragmentManager, it.postId)
        }

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.putExtra(IKEY_LOGIN, true)
            startActivityForResult(intent, SIGN_IN_REQUEST_CODE)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                SIGN_IN_REQUEST_CODE -> {
                    if (requireActivity().getToken().isNullOrEmpty())
                        binding.layoutSignIn.visibility = View.VISIBLE
                    else {
                        binding.layoutSignIn.visibility = View.GONE
                        viewModel.getFavorites(requireActivity().getToken())
                    }
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}