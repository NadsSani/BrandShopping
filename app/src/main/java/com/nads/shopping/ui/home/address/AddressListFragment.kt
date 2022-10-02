package com.nads.shopping.ui.home.address

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
import com.nads.shopping.databinding.FragmentAddressListBinding
import com.nads.shopping.utils.getToken
import com.nads.shopping.viewmodels.AddressViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory

class AddressListFragment : Fragment() {

    companion object {
        fun newInstance() =
            AddressListFragment()
    }

    private val args: AddressListFragmentArgs by navArgs()
    private val viewModel: AddressViewModel by viewModels()
    private lateinit var binding: FragmentAddressListBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_address_list, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeActivityViewModel.showTitle.value = true
        homeActivityViewModel.showSearchBar.value = false
        homeActivityViewModel.showBack.value = true
        homeActivityViewModel.showSearchIcon.value = true

        homeActivityViewModel.title.value = getString(R.string.shipping_address_title)

        viewModel.addressSelectEvent.observe(viewLifecycleOwner) {
            homeActivityViewModel.selectedAddress.value = it

            if (args.isEditMode) {
                val destination =
                    AddressListFragmentDirections.actionNavAddressListToNavAddAddress(true, it.streetNumber,it.addressName, it.zoneNumber, it.buildingNumber, it.id)
                findNavController().navigate(destination)
            } else
                requireActivity().onBackPressed()
        }

        binding.layoutProceed.setOnClickListener {
            findNavController().navigate(R.id.nav_add_address)
        }

        viewModel.getAddressList(requireActivity().getToken())
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}