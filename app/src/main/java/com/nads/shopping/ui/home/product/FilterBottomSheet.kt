package com.nads.shopping.ui.home.product

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nads.shopping.R
import com.nads.shopping.databinding.FilterBottomSheetBinding
import com.nads.shopping.viewmodels.FilterViewModel
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory


class FilterBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FilterBottomSheetBinding
    private val viewModel: FilterViewModel by viewModels()

    private val homeActivityViewModel: HomeActivityViewModel by activityViewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.filter_bottom_sheet, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnApply.setOnClickListener {

            homeActivityViewModel.selectedFilters.clear()
            homeActivityViewModel.selectedFilters.addAll(viewModel.selectedFilters)
            homeActivityViewModel.isPriceLH.value=viewModel.isPriceLH.value
            viewModel.selectedFilters.clear()

            homeActivityViewModel.filterSelectEvent.value = true

            dismiss()
        }

        viewModel.isPriceLH.value = homeActivityViewModel.isPriceLH.value
        viewModel.selectedFilters.addAll(homeActivityViewModel.selectedFilters)

        viewModel.getCategories()
        viewModel.getBrands()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NORMAL, R.style.AppTheme_Filter)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
        }
    }
}