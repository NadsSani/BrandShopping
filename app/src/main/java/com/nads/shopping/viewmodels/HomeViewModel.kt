package com.nads.shopping.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.BR
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.Banner
import com.nads.shopping.repositories.BannerRepository
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

class HomeViewModel : BaseViewModel() {
    val loading = MutableLiveData<Boolean>()
    val banners = ObservableArrayList<Banner>()

    private val showMessage = LiveEvent<String>()
    val showMessageEvent = showMessage

    init {
        getBanners()
    }

    val repository = BannerRepository(ApiFactory.brandattyAPI)

    val bannerItemBinding =
        ItemBinding.of<Banner>(BR.banner, R.layout.home_banner_item)

    fun getBanners() {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getBanners()

            loading.postValue(false)

            response?.let {
                it.data?.let {bannerList->
                    banners.addAll(bannerList)
                }
            }
        }
    }

    fun showMessage(message: String) {
        showMessage.value = message

        Handler(Looper.getMainLooper()).postDelayed({
            showMessage.value = "0"
        }, 2000)
    }
}