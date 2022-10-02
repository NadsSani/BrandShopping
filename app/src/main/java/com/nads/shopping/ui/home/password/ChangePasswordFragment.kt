package com.nads.shopping.ui.home.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentChangePasswordBinding
import com.nads.shopping.utils.createSnack
import com.nads.shopping.utils.getToken
import com.nads.shopping.utils.isEnglish
import com.nads.shopping.viewmodels.ChangePasswordViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory

class ChangePasswordFragment : Fragment() {

    companion object {
        fun newInstance() =
            ChangePasswordFragment()
    }

    private val viewModel: ChangePasswordViewModel by viewModels()
    private lateinit var binding: FragmentChangePasswordBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeActivityViewModel.showTitle.value = true
        homeActivityViewModel.showSearchBar.value = false
        homeActivityViewModel.showBack.value = true
        homeActivityViewModel.showSearchIcon.value = true

        homeActivityViewModel.title.value = getString(R.string.change_password_caps)

        viewModel.changePasswordEvent.observe(viewLifecycleOwner) {
            binding.layoutProceed.createSnack(if (requireActivity().isEnglish()) it.message else it.messageAr)

            if (it.status == 200)
                requireActivity().onBackPressed()
        }

        binding.layoutProceed.setOnClickListener {
            viewModel.changePassword(requireActivity().getToken())
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}