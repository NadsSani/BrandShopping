package com.nads.shopping.ui.home.profile

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
import androidx.preference.PreferenceManager
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentProfileBinding
import com.nads.shopping.ui.activities.LoginActivity
import com.nads.shopping.utils.*
import com.nads.shopping.viewmodels.AccountViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() =
            ProfileFragment()
    }

    private val viewModel: AccountViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        homeActivityViewModel.showTitle.value = false
        homeActivityViewModel.showSearchBar.value = false
        homeActivityViewModel.showSearchIcon.value = true
        homeActivityViewModel.showBack.value = true

        homeActivityViewModel.title.value = getString(R.string.account)

        if (requireActivity().getToken().isNullOrEmpty())
            binding.layoutSignIn.visibility = View.VISIBLE
        else {
            binding.layoutSignIn.visibility = View.GONE
            viewModel.getBasicInfo(requireActivity().getToken())
        }

        viewModel.getBasicInfo(requireActivity().getToken())

        binding.tvProfile.setOnClickListener {
            findNavController().navigate(R.id.nav_account)
        }

        binding.tvWishList.setOnClickListener {
            findNavController().navigate(R.id.nav_favorites)
        }

        binding.tvChangePassword.setOnClickListener {
            findNavController().navigate(R.id.nav_change_password)
        }

        binding.tvShippingAddress.setOnClickListener {
            val destination = ProfileFragmentDirections.actionNavProfileToNavAddressList(true)
            findNavController().navigate(destination)
        }

        binding.tvMyOrders.setOnClickListener {
            findNavController().navigate(R.id.nav_orders)
        }

        binding.tvCoupons.setOnClickListener {
            findNavController().navigate(R.id.nav_coupon)
        }

        val language: String? = defaultPrefs(requireActivity())[PREF_KEY_SELECTED_LANGUAGE, "en"]
        if (language == "en")
            binding.rbEnglish.isChecked = true
        else binding.rbArabic.isChecked = true

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, id ->

            if (id == R.id.rbEnglish) {
                defaultPrefs(requireActivity())[PREF_KEY_SELECTED_LANGUAGE] = "en"
                setLocale(requireActivity())
            } else {
                defaultPrefs(requireActivity())[PREF_KEY_SELECTED_LANGUAGE] = "ar"
                setLocale(requireActivity())
            }

        }

        binding.tvLogout.setOnClickListener {
            val language: String? =
                defaultPrefs(requireActivity())[PREF_KEY_SELECTED_LANGUAGE, "en"]
            PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().apply()
            defaultPrefs(requireActivity())[PREF_KEY_SELECTED_LANGUAGE] = language
//                 defaultPrefs(requireActivity())[PKEY_FIRST_TIME] = false
            requireActivity().finish()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.putExtra(IKEY_LOGIN, true)
            startActivityForResult(intent, SIGN_IN_REQUEST_CODE)
        }

        return binding.root
    }

    fun setLocale(context: Activity) {
        context.recreate()
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
                        viewModel.getBasicInfo(requireActivity().getToken())
                    }
                }
            }
    }

}