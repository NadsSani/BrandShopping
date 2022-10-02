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
import com.nads.shopping.databinding.FragmentCheckoutCardBinding
import com.nads.shopping.viewmodels.OrderViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory

class CheckoutCardFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutCardBinding
    private val viewModel: OrderViewModel by viewModels()

    private val args:CheckoutCardFragmentArgs by navArgs()
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_checkout_card, container, false)
        binding.viewModel=viewModel
        binding.lifecycleOwner=viewLifecycleOwner
        homeActivityViewModel.showTitle.value = true
        homeActivityViewModel.showSearchBar.value = false
        homeActivityViewModel.showBack.value = true
        homeActivityViewModel.showSearchIcon.value = false

        homeActivityViewModel.title.value = "CHECKOUT"

        binding.layoutProceed.setOnClickListener {
                val destination =
                    CheckoutCardFragmentDirections.actionNavCheckoutCardToNavCheckoutSummary(
                        args.street,
                        args.zone,
                        args.building,
                    )

                findNavController().navigate(destination)
        }

        return binding.root
    }
}