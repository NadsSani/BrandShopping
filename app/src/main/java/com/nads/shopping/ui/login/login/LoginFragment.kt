package com.nads.shopping.ui.login.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nads.shopping.R
import com.nads.shopping.databinding.ForgotPasswordDialogBinding
import com.nads.shopping.databinding.FragmentLoginBinding
import com.nads.shopping.ui.activities.HomeActivity
import com.nads.shopping.utils.*
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.LoginViewModel
import com.nads.shopping.viewmodels.ViewModelFactory

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() =
            LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val isFromLogin = requireActivity().intent.getBooleanExtra(IKEY_LOGIN, false)

        viewModel.loginResponse.observe(viewLifecycleOwner) {
            if (it.status == 200) {
                defaultPrefs(requireActivity())[PKEY_TOKEN] = it.data?.token
                defaultPrefs(requireActivity())[PKEY_DP] = it.data?.photo
                requireActivity().setResult(Activity.RESULT_OK, requireActivity().intent)
                requireActivity().finish()

                if (!isFromLogin)
                    startActivity(Intent(requireActivity(), HomeActivity::class.java))
            } else {
                binding.btnSignIn.createSnack(it.message)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            forgotPasswordDialog()
        }

        binding.ivSkip.setOnClickListener {
            requireActivity().finish()
            startActivity(
                Intent(
                    requireActivity(),
                    HomeActivity::class.java
                )
            )
        }
        binding.tvSignup.setOnClickListener { findNavController().navigate(R.id.nav_sugn_up) }
//        binding.tvForgotPassword.setOnClickListener { findNavController().navigate(R.id.nav_reset_password) }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    private fun forgotPasswordDialog() {
        var alertDialog: AlertDialog? = null
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        val dialogBinding: ForgotPasswordDialogBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireActivity()),
                R.layout.forgot_password_dialog,
                null,
                false
            )
        val dialogView: View = dialogBinding.root
        dialogBinding.viewModel = viewModel
        dialogBinding.lifecycleOwner = this

        viewModel.resetPasswordResponseEvent.observe(viewLifecycleOwner) {
            if (it.status == 200) {
                alertDialog?.dismiss()
                binding.root.createSnack(if (requireActivity().isEnglish()) it.message else it.messageAr)
            } else dialogBinding.etEmail.error =
                if (requireActivity().isEnglish()) it.message else it.messageAr
        }

        dialogBinding.btnSend.setOnClickListener {
            viewModel.forgotPassword()
        }

        dialogBinding.btnDismiss.setOnClickListener {
            alertDialog?.dismiss()
        }

        builder.setView(dialogView)
        alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialog.show()
    }
}