package com.nads.shopping.ui.home.coupon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentCouponListBinding
import com.nads.shopping.utils.copyToClipboard
import com.nads.shopping.utils.createSnack
import com.nads.shopping.viewmodels.CouponViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory


class CouponListFragment : Fragment() {

    companion object {
        fun newInstance() =
            CouponListFragment()
    }

    private val viewModel: CouponViewModel by viewModels()
    private lateinit var binding: FragmentCouponListBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_coupon_list, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeActivityViewModel.showTitle.value = false
        homeActivityViewModel.showSearchBar.value = true
        homeActivityViewModel.showBack.value = true
        homeActivityViewModel.showSearchIcon.value = false

        viewModel.couponSelectEvent.observe(viewLifecycleOwner){
            requireActivity().copyToClipboard(it.code)
            binding.root.createSnack(getString(R.string.coupon_copied))
        }

        viewModel.getCoupon()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}