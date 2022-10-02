package com.nads.shopping.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.BR
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.*
import com.nads.shopping.listeners.ItemSelectListener
import com.nads.shopping.repositories.CouponRepository
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class CouponViewModel : BaseViewModel() {
    val loading = MutableLiveData<Boolean>()

    val couponList = ObservableArrayList<Coupon>()

    private val repository = CouponRepository(ApiFactory.brandattyAPI)

    private val couponSelect = LiveEvent<Coupon>()
    val couponSelectEvent = couponSelect

    val couponSelectListener = object : ItemSelectListener<Coupon> {
        override fun onItemSelected(item: Coupon) {
            couponSelect.value = item
        }

    }

    val couponItemBinding: OnItemBindClass<Coupon> = OnItemBindClass<Coupon>().map(
        Coupon::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.coupon, R.layout.coupon_item)

        itemBinding.bindExtra(BR.listener, couponSelectListener)
    }

    fun getCoupon() {
        mainScope.launch {
            loading.postValue(true)
            val response =
                repository.getCoupons()

            loading.postValue(false)

            response?.data?.let {
                couponList.clear()
                couponList.addAll(it)
            }
        }
    }

}