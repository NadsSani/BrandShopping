package com.nads.shopping.ui.home.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nads.shopping.R
import com.nads.shopping.databinding.FragmentSelectCatFirstBinding
import com.nads.shopping.viewmodels.CategoriesViewModel

//New Edits are added comments starts with nds
//nds The First Page starts from here

class SelectCatFirst : Fragment() {
    private val catViewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding:FragmentSelectCatFirstBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_select_cat_first,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.catViewModel = catViewModel
        directbuttons(binding)
        return binding.root
    }



    fun directbuttons(binding:FragmentSelectCatFirstBinding){
        val appbar = requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout)
        appbar.isVisible = false
        val bottomnavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomnavigation.isVisible =false
        catViewModel.getCategories()
        binding.dressbusbutton.setOnClickListener{

              findNavController().navigate(R.id.dressBus)
        }
        binding.toysbutton.setOnClickListener{

            val directions = SelectCatFirstDirections.actionSelectCatFirstToNavHome(selectedcat = 1)

            findNavController().navigate(directions)
        }
        binding.toysbusbutton.setOnClickListener{

            findNavController().navigate(R.id.toysBus)
        }
        binding.dressbutton.setOnClickListener{
            val directions = SelectCatFirstDirections.actionSelectCatFirstToNavHome(selectedcat = 2)

            findNavController().navigate(directions)
        }
    }
}