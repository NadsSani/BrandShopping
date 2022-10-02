package com.nads.shopping.ui.home.cart

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
import androidx.navigation.fragment.findNavController
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentCartBinding
import com.nads.shopping.ui.activities.LoginActivity
import com.nads.shopping.utils.IKEY_LOGIN
import com.nads.shopping.utils.SIGN_IN_REQUEST_CODE
import com.nads.shopping.utils.createSnack
import com.nads.shopping.utils.getToken
import com.nads.shopping.viewmodels.CartViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory

class CartFragment : Fragment() {

    companion object {
        fun newInstance() =
            CartFragment()
    }

    private val viewModel: CartViewModel by viewModels()
    private lateinit var binding: FragmentCartBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeActivityViewModel.showTitle.value = true
        homeActivityViewModel.showSearchBar.value = false
        homeActivityViewModel.showBack.value = true
        homeActivityViewModel.showSearchIcon.value = true

        homeActivityViewModel.title.value =
            "${getString(R.string.cart)} (${homeActivityViewModel.cartCount.value})"

        binding.layoutProceed.setOnClickListener {
            if (homeActivityViewModel.cartCount.value ?: 0 > 0){
                val destination=CartFragmentDirections.actionNavCartToNavCheckoutAddress(viewModel.couponCode.value.toString())
                findNavController().navigate(destination)
            }
            else it.createSnack("Cart is empty")
        }

        if (requireActivity().getToken().isNullOrEmpty())
            binding.layoutSignIn.visibility = View.VISIBLE
        else {
            binding.layoutSignIn.visibility = View.GONE
            if (viewModel.cartList.isEmpty())
                viewModel.getCartList(requireActivity().getToken())
        }

        homeActivityViewModel.getCartCount(requireActivity().getToken())

        homeActivityViewModel.cartCount.observe(viewLifecycleOwner) {
            homeActivityViewModel.title.value = "CART (${homeActivityViewModel.cartCount.value})"
        }

        viewModel.cartListResponse.observe(viewLifecycleOwner) {
            homeActivityViewModel.getCartCount(requireActivity().getToken())
        }

        viewModel.applyCouponResponseEvent.observe(viewLifecycleOwner) {
            if (it.status == 200) {
                val cartResponse = viewModel.cartListResponse

                cartResponse.value?.priceDetails?.subTotal =
                    it.data?.priceDetails?.subTotal.toString()
                cartResponse.value?.priceDetails?.total = it.data?.priceDetails?.total.toString()
                cartResponse.value?.priceDetails?.deliveryPrice =
                    it.data?.priceDetails?.deliveryPrice.toString()
                cartResponse.value?.priceDetails?.discount =
                    it.data?.priceDetails?.discount.toString()

                viewModel.cartListResponse.value = cartResponse.value
            }
        }

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.putExtra(IKEY_LOGIN, true)
            startActivityForResult(intent, SIGN_IN_REQUEST_CODE)
        }

        binding.btnApplyCoupon.setOnClickListener {
            viewModel.applyCoupon(requireActivity().getToken())
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
                        viewModel.getCartList(requireActivity().getToken())
                    }
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}