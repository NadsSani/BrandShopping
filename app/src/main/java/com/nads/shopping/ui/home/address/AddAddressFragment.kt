package com.nads.shopping.ui.home.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentAddAddressBinding
import com.nads.shopping.utils.createSnack
import com.nads.shopping.utils.getToken
import com.nads.shopping.utils.isEnglish
import com.nads.shopping.viewmodels.AddressViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory

class AddAddressFragment : Fragment() {

    companion object {
        fun newInstance() =
            AddAddressFragment()
    }

    private val args: AddAddressFragmentArgs by navArgs()
    private val viewModel: AddressViewModel by viewModels()
    private lateinit var binding: FragmentAddAddressBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeActivityViewModel.showTitle.value = true
        homeActivityViewModel.showSearchBar.value = false
        homeActivityViewModel.showBack.value = true
        homeActivityViewModel.showSearchIcon.value = true

        homeActivityViewModel.title.value = getString(R.string.add_address)

        if (args.isEditMode) {
            viewModel.name.value = args.name
            viewModel.street.value = args.street
            viewModel.zone.value = args.zone
            viewModel.building.value = args.building
            viewModel.submitButtonText.value=R.string.update_caps
            homeActivityViewModel.title.value = getString(R.string.update_address)
        }

        viewModel.addAddressResponseEvent.observe(viewLifecycleOwner) {
            binding.layoutProceed.createSnack(if (requireActivity().isEnglish()) it.message else it.messageAr)
            requireActivity().onBackPressed()
        }

        viewModel.editAddressResponseEvent.observe(viewLifecycleOwner) {
            binding.layoutProceed.createSnack(if (requireActivity().isEnglish()) it.message else it.messageAr)
            requireActivity().onBackPressed()
        }

        binding.layoutProceed.setOnClickListener {
            if (args.isEditMode)
                viewModel.editAddress(requireActivity().getToken(), args.addressId)
            else viewModel.addAddress(requireActivity().getToken())
        }

        viewModel.getAddressList(requireActivity().getToken())
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}