package com.nads.shopping.ui.home.checkout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentCheckoutSummaryBinding
import com.nads.shopping.utils.createSnack
import com.nads.shopping.utils.getToken
import com.nads.shopping.viewmodels.CartViewModel
import com.nads.shopping.viewmodels.OrderViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory

class CheckoutSummaryFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutSummaryBinding
    private val viewModel: CartViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
    private val args: CheckoutSummaryFragmentArgs by navArgs()

    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_checkout_summary, container, false)
        binding.viewModel = viewModel
        binding.checkoutViewModel = orderViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeActivityViewModel.showTitle.value = true
        homeActivityViewModel.showSearchBar.value = false
        homeActivityViewModel.showBack.value = true
        homeActivityViewModel.showSearchIcon.value = false

        homeActivityViewModel.title.value = getString(R.string.checkout)

        binding.layoutProceed.setOnClickListener {

            orderViewModel.placeOrder(
                requireActivity().getToken(),
                args.building,
                args.zone,
                args.street,
                if (args.couponCode == "0") "" else args.couponCode
            )

        }

        orderViewModel.placeOrderResponseEvent.observe(viewLifecycleOwner) {
            if (it.success.status == 200) {
                val destination =
                    CheckoutSummaryFragmentDirections.actionNavCheckoutSummaryToNavHome("Your order(${it.success.data?.orderId}) has been Placed.")

                binding.layoutSuccess.visibility=View.VISIBLE

                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(destination)
                },5000)

            } else {
                binding.layoutProceed.createSnack(it.success.message)
            }
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

        if (args.couponCode != "0"){
            viewModel.couponCode.value=args.couponCode
            viewModel.applyCoupon(requireActivity().getToken())
        }

        if (viewModel.cartList.isEmpty())
            viewModel.getCartList(requireActivity().getToken())

        return binding.root
    }
}