package com.nads.shopping.ui.home.product

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentProductDetailsBinding
import com.nads.shopping.ui.activities.LoginActivity
import com.nads.shopping.utils.IKEY_LOGIN
import com.nads.shopping.utils.SIGN_IN_REQUEST_CODE
import com.nads.shopping.utils.createSnack
import com.nads.shopping.utils.getToken
import com.nads.shopping.viewmodels.*


class ProductDetailsDialog() : DialogFragment() {
    val TAG = "example_dialog"

    private val cartViewModel: CartViewModel by viewModels()
    private val viewModel: ProductsViewModel by viewModels()
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var binding: FragmentProductDetailsBinding
    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    private var productId: String? = ""

    companion object {
        fun display(fragmentManager: FragmentManager, postId: String): ProductDetailsDialog {

            val dialog = ProductDetailsDialog()
            var args = Bundle()
            args.putString("post_id", postId)
            dialog.arguments = args
            Log.e("postid",postId)
            dialog.show(fragmentManager, "details_dialog")
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog_NoStatusBar)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false)
        binding.viewModel = viewModel
        binding.cartViewModel = cartViewModel
        binding.activityViewModel = homeActivityViewModel
        binding.favoritesViewModel = favoritesViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        PagerSnapHelper().attachToRecyclerView(binding.rvBanner)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.indicator.attachToRecyclerView(binding.rvBanner)
        }, 2000)

        favoritesViewModel.addToFavoriteResponse.observe(viewLifecycleOwner) {
            if (it.status == 200) {
                val response = viewModel.productDetailsResponse.value
                response?.isFavorite = 1

                viewModel.productDetailsResponse.value = response
            }

            binding.layoutAddToCart.createSnack(it.message)
        }

        cartViewModel.addCartResponseEvent.observe(viewLifecycleOwner) {
            if (it.status == 200)
                homeActivityViewModel.getCartCount(requireActivity().getToken())

            binding.layoutAddToCart.createSnack(it.message)
        }

        viewModel.sizeSelectEvent.observe(viewLifecycleOwner) {

            // update inventory details
            var options = ""
            viewModel.selectedOptions?.forEachIndexed { index, item ->
                options += item

                if (index < viewModel.selectedOptions.size - 1)
                    options += "-"
            }

            viewModel.getInventory(productId.toString(), options)
        }

        viewModel.productSelectEvent.observe(viewLifecycleOwner) {
            ProductDetailsDialog.display(childFragmentManager, it.productsId)
        }

        binding.ivBack.setOnClickListener {
            dismiss()
        }

        binding.ivCart.setOnClickListener {
            parentFragment?.findNavController()?.navigate(R.id.nav_cart)
        }

        binding.layoutAddToCart.setOnClickListener {
            if (requireActivity().getToken().isNullOrEmpty()) {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.putExtra(IKEY_LOGIN, true)
                startActivityForResult(intent, SIGN_IN_REQUEST_CODE)
            } else
                cartViewModel.addCart(
                    requireActivity().getToken(),
                    viewModel.productDetailsResponse.value?.productId.toString(),
                    viewModel.addCartQuantity.value.toString(),
                    viewModel.productDetailsResponse.value?.inventoryId.toString()
                )

        }

        binding.btnAddToFavorites.setOnClickListener {
            val isFavorite = if (viewModel.productDetailsResponse.value?.isFavorite == 0) 1 else 0

            if (requireActivity().getToken().isNullOrEmpty()) {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.putExtra(IKEY_LOGIN, true)
                startActivityForResult(intent, SIGN_IN_REQUEST_CODE)
            } else
                favoritesViewModel.addToFavorites(
                    requireActivity().getToken(),
                    productId.toString(),
                    isFavorite
                )
        }

        arguments?.let {
            productId = it.getString("post_id")
            viewModel.getProductDetails(productId.toString())
        }



        return binding.root
    }

    fun Bundle?.onViewCreated(view: View) {
        super.onViewCreated(view, this)

    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION
            );
        }
    }
}