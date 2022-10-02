package com.nads.shopping.ui.login.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentSignUpBinding
import com.nads.shopping.utils.createSnack
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.SignUpViewModel
import com.nads.shopping.viewmodels.ViewModelFactory

class SignUpFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var binding: FragmentSignUpBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.loginResponse.observe(viewLifecycleOwner) {
            if (it.status == 200) {
                requireActivity().onBackPressed()
            }
            binding.btnSignIn.createSnack(it.message)
        }

        binding.tvSignIn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}
