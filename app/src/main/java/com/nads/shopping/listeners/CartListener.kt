package com.nads.shopping.listeners

import android.view.View
import com.nads.shopping.datamodels.CartItem

interface CartListener {
    fun onQuantityPlus(view:View, item: CartItem)
    fun onQuantityMinus(view:View, item: CartItem)
    fun onItemSelected(item: CartItem)
    fun onRemoveSelected(view:View, item: CartItem)
}