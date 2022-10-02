package com.nads.shopping.listeners

import android.view.View
import com.nads.shopping.datamodels.FavoritesItem

interface FavoritesListener {
    fun onItemSelected(item: FavoritesItem)
    fun onRemoveSelected(view:View, item: FavoritesItem)
}