package com.nads.shopping.ui.home.checkout

import android.os.Bundle
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
import com.nads.shopping.databinding.FragmentCheckoutAddressBinding
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.OrderViewModel
import com.nads.shopping.viewmodels.ViewModelFactory

class CheckoutAddressFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutAddressBinding
    private val viewModel: OrderViewModel by viewModels()
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }
    private val args: CheckoutAddressFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_checkout_address, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeActivityViewModel.showTitle.value = true
        homeActivityViewModel.showSearchBar.value = false
        homeActivityViewModel.showBack.value = true
        homeActivityViewModel.showSearchIcon.value = false

        homeActivityViewModel.title.value = getString(R.string.checkout)

        homeActivityViewModel.selectedAddressEvent.observe(requireActivity()) {
            viewModel.name.value = it.addressName
            viewModel.street.value = it.streetNumber
            viewModel.zone.value = it.zoneNumber
            viewModel.building.value = it.buildingNumber
        }

        binding.layoutProceed.setOnClickListener {
            if (viewModel.validateAddress()) {
                val destination =
                    CheckoutAddressFragmentDirections.actionNavCheckoutAddressToNavCheckoutSummary(
                        viewModel.street.value.toString(),
                        viewModel.zone.value.toString(),
                        viewModel.building.value.toString(),
                         args.couponCode
                    )

                findNavController().navigate(destination)
            }
        }

        binding.tvSelectAddress.setOnClickListener {
            findNavController().navigate(R.id.nav_address_list)
        }

        return binding.root
    }
}