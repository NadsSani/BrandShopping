package com.nads.shopping.ui.home.search

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentSearchBinding
import com.nads.shopping.ui.home.product.ProductDetailsDialog
import com.nads.shopping.viewmodels.ProductsViewModel
import com.nads.shopping.viewmodels.SearchViewModel


class SearchDialog() : DialogFragment() {
    val TAG = "example_dialog"

    private val productViewModel: ProductsViewModel by viewModels()
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding

    companion object {
        fun display(fragmentManager: FragmentManager): SearchDialog {

            val exampleDialog = SearchDialog()
            exampleDialog.show(fragmentManager, "search_dialog")
            return exampleDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.productViewModel = productViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.ivBack.setOnClickListener {
            dismiss()
        }

        productViewModel.productSelectEvent.observe(viewLifecycleOwner) {
            ProductDetailsDialog.display(childFragmentManager, it.productsId)
        }

        binding.etSearch.setOnEditorActionListener { textView, actionId, event ->
            val imeAction = when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> true
                else -> false
            }

            val keydownEvent = event?.keyCode == KeyEvent.KEYCODE_ENTER
                    && event.action == KeyEvent.ACTION_DOWN

            if (imeAction or keydownEvent)
                true.also { productViewModel.getProducts() }
            else false
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
        }
    }
}