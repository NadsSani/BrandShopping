package com.nads.shopping.ui.home.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentAccountBinding
import com.nads.shopping.databinding.PermissionDialogBinding
import com.nads.shopping.utils.*
import com.nads.shopping.viewmodels.AccountViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.image_selector_dialog.*
import java.io.File
import java.util.*

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() =
            AccountFragment()
    }

    private val viewModel: AccountViewModel by viewModels()
    private lateinit var binding: FragmentAccountBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    private lateinit var capturedUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeActivityViewModel.showTitle.value = true
        homeActivityViewModel.showSearchBar.value = false
        homeActivityViewModel.showSearchIcon.value = true
        homeActivityViewModel.showBack.value = true

        homeActivityViewModel.title.value = getString(R.string.account_title)

        binding.etDob.transformIntoDatePicker(requireActivity(), "MM/dd/yyyy", Date())

        viewModel.profileUpdateEvent.observe(viewLifecycleOwner) {
            it.let { binding.btnUpdate.createSnack(if(requireActivity().isEnglish()) it.message else it.messageAr) }
        }

        /*  binding.btnChangePassword.setOnClickListener {
              findNavController().navigate(R.id.nav_change_password)
          }*/

        binding.editProfilePic.setOnClickListener {
            createImageUploadDialog()
        }

        binding.btnUpdate.setOnClickListener {
            val gender = if (binding.rbMale.isChecked) "Male" else "Female"
            viewModel.updateBasicInfo(requireActivity().getToken(), gender)
        }

        viewModel.profileLoadEvent.observe(viewLifecycleOwner) {
            if (it.status == 200) {
                if (it.data?.gender == "Male")
                    binding.rbMale.isChecked = true
                else binding.rbFemale.isChecked = true
            }
        }

        viewModel.getBasicInfo(requireActivity().getToken())

        return binding.root
    }

    private fun createImageUploadDialog() {
        val dialog: Dialog = Dialog(requireActivity(), R.style.ThemeDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.image_selector_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.tvGallery.setOnClickListener {

            Dexter.withContext(requireActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
                        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                        galleryIntent.type = "image/*"
                        startActivityForResult(
                            Intent.createChooser(
                                galleryIntent,
                                "Select Profile Pic"
                            ),
                            GALLERY_REQUEST_CODE
                        )
                        dialog.dismiss()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        permissionRationaleDialog(token)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(
                            requireActivity(),
                            "Please select image",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }).check()

        }

        dialog.tvCamera.setOnClickListener {
            Dexter.withContext(requireActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        capturedUri = FileProvider.getUriForFile(
                            requireActivity(),
                            "com.whytecreations.brandatty.provider", //(use your app signature + ".provider" )
                            File(
                                requireActivity().filesDir.path,
                                "pic_" + System.currentTimeMillis().toString() + ".jpg"
                            )
                        )

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUri)
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                        dialog.dismiss()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        permissionRationaleDialog(token)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(
                            requireActivity(),
                            "Please allow to take photo",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }).check()
        }

        dialog.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun permissionRationaleDialog(token: PermissionToken?) {
        var alertDialog: AlertDialog? = null
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        val dialogBinding: PermissionDialogBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireActivity()),
                R.layout.permission_dialog,
                null,
                false
            )
        val dialogView: View = dialogBinding.root
        dialogBinding.lifecycleOwner = this

        dialogBinding.btnOk.setOnClickListener {
            alertDialog?.dismiss()
            token?.continuePermissionRequest()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
//                    val selectedPhoto = data?.extras?.get("data") as Bitmap
                    val finalFile = capturedUri?.getFile(requireActivity())
                    val path: String =
//                        ImageCompressor.compressImage(context, "/data/user/0/com.whytecreations.sam/cache/IMG_20201016_165221.jpg")
                        ImageCompressor.compressImage(context, finalFile?.absolutePath)
                    viewModel.selectedImage.value = File(path)
                    Glide.with(requireActivity()).load(finalFile).into(binding.ivProfilePic)
                }

                GALLERY_REQUEST_CODE -> {
                    val tempUri: Uri? = data?.data
                    val finalFile = tempUri?.getFile(requireActivity())
                    val path: String =
                        ImageCompressor.compressImage(context, finalFile?.absolutePath)
                    viewModel.selectedImage.value = File(path)
                    Glide.with(requireActivity()).load(finalFile).into(binding.ivProfilePic)
                }
            }
    }
}