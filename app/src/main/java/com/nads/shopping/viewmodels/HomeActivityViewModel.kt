package com.nads.shopping.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.Address
import com.nads.shopping.datamodels.SelectedFilter
import com.nads.shopping.repositories.CartRepository
import kotlinx.coroutines.launch

class HomeActivityViewModel : BaseViewModel() {


    val cartCount = MutableLiveData(0)

    val title = MutableLiveData<String>()
    val showBack = MutableLiveData<Boolean>()
    val showTitle = MutableLiveData<Boolean>()
    val showSearchIcon = MutableLiveData<Boolean>()
    val showSearchBar = MutableLiveData<Boolean>()
    val isPriceLH = MutableLiveData<Boolean>(true)

    val selectedFilters = ObservableArrayList<SelectedFilter>()

    val selectedAddress = LiveEvent<Address>()
    val selectedAddressEvent = selectedAddress

    private val filterSelect = LiveEvent<Boolean>()
    val filterSelectEvent = filterSelect

    val repository = CartRepository(ApiFactory.brandattyAPI)

    init {
        title.value = "Brandatty"
        showBack.value = false
    }

    companion object {
        private var instance: HomeActivityViewModel? = null
        fun getInstance() =
            instance ?: synchronized(HomeActivityViewModel::class.java) {
                instance ?: HomeActivityViewModel().also { instance = it }
            }
    }

    fun getCartCount(token: String) {
        mainScope.launch {
            val response = repository.getCartCount(token)

            response?.let {
                cartCount.value = it.count
            }
        }
    }

}