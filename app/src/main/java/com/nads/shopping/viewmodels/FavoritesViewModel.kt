package com.nads.shopping.viewmodels

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.BR
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.BasicResponse
import com.nads.shopping.datamodels.FavoritesItem
import com.nads.shopping.listeners.FavoritesListener
import com.nads.shopping.repositories.FavoritesRepository
import com.nads.shopping.utils.getToken
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class FavoritesViewModel : BaseViewModel() {
    val addToFavLoading = MutableLiveData<Boolean>()
    val addToFavoriteResponse = MutableLiveData<BasicResponse>()
    val favorites = ObservableArrayList<FavoritesItem>()
    var lastSelectedCartItem = 0

    private val favoritesItemSelect = LiveEvent<FavoritesItem>()
    val favoritesItemSelectEvent = favoritesItemSelect

    private val repository = FavoritesRepository(ApiFactory.brandattyAPI)

    private val favoriteListener = object : FavoritesListener {

        override fun onItemSelected(item: FavoritesItem) {
            favoritesItemSelect.value = item
        }

        override fun onRemoveSelected(view: View, item: FavoritesItem) {
            addToFavorites(view.context.getToken(), item.postId, 0)
        }
    }

    init {
    }

    val favoritesItemBinding: OnItemBindClass<FavoritesItem> = OnItemBindClass<FavoritesItem>().map(
        FavoritesItem::class.java
    ) { itemBinding, position, item ->
        /* if (item.isSelected)
             itemBinding.set(BR.topupItem, R.layout.topup_item_selected)
         else*/ itemBinding.set(BR.favorite, R.layout.favorites_item)

        itemBinding.bindExtra(BR.listener, favoriteListener)
    }

    fun addToFavorites(token: String, productId: String, isFavorite: Int) {

        mainScope.launch {
            addToFavLoading.postValue(true)
            val response = repository.addToFavorites(token, productId, isFavorite)

            addToFavLoading.postValue(false)

            response?.let {
                addToFavoriteResponse.postValue(it)

                if (isFavorite == 0) {
                    val list = favorites.filter { it.postId == productId }
                    if (list.isNotEmpty()) favorites.remove(list[0])
                }
            }
        }
    }

    fun getFavorites(token: String) {

        mainScope.launch {
            addToFavLoading.postValue(true)
            val response = repository.getFavoritesAsync(token)

            addToFavLoading.postValue(false)

            response?.data?.let {
                favorites.clear()
                favorites.addAll(it)
            }
        }
    }
}