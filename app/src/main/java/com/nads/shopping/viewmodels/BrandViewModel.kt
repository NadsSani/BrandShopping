package com.nads.shopping.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.BR
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.*
import com.nads.shopping.listeners.ItemSelectListener
import com.nads.shopping.repositories.BrandRepository
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class BrandViewModel : BaseViewModel() {
    val loading = MutableLiveData<Boolean>()

    val brandList = ObservableArrayList<Brand>()

    private val repository = BrandRepository(ApiFactory.brandattyAPI)

    private val brandSelect = LiveEvent<Brand>()
    val brandSelectEvent = brandSelect

    val brandSelectListener = object : ItemSelectListener<Brand> {
        override fun onItemSelected(item: Brand) {
            brandSelect.value = item
        }

    }

    val brandItemBinding: OnItemBindClass<Brand> = OnItemBindClass<Brand>().map(
        Brand::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.brand, R.layout.home_brand_item)

        itemBinding.bindExtra(BR.listener, brandSelectListener)
    }

    val brandRoundItemBinding: OnItemBindClass<Brand> = OnItemBindClass<Brand>().map(
        Brand::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.brand, R.layout.brand_item)

        itemBinding.bindExtra(BR.listener, brandSelectListener)
    }

    fun getBrands() {
        mainScope.launch {
            loading.postValue(true)
            val response =
                repository.getBrands()

            loading.postValue(false)

            response?.data?.let {
                brandList.clear()
                brandList.addAll(it)
            }
        }
    }

}