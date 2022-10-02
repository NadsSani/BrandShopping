package com.nads.shopping.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.BR
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.OptionAttribute
import com.nads.shopping.datamodels.Order
import com.nads.shopping.datamodels.PlaceOrderResponse
import com.nads.shopping.repositories.OrderRepository
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class OrderViewModel : BaseViewModel() {
    val loading = MutableLiveData<Boolean>()

    val orderHistoryList = ObservableArrayList<Order>()
    val name = MutableLiveData<String>()
    val street = MutableLiveData<String>()
    val zone = MutableLiveData<String>()
    val building = MutableLiveData<String>()

    val streetError = MutableLiveData<String>()
    val zoneError = MutableLiveData<String>()
    val buildingError = MutableLiveData<String>()

    private val placeOrderResponse = LiveEvent<PlaceOrderResponse>()
    val placeOrderResponseEvent = placeOrderResponse

    private val repository = OrderRepository(ApiFactory.brandattyAPI)

    val orderHistoryItemBinding: OnItemBindClass<Order> = OnItemBindClass<Order>().map(
        Order::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.orderItem, R.layout.order_item)

        itemBinding.bindExtra(BR.viewModel, this)
    }

    val optionsKeyBinding: OnItemBindClass<OptionAttribute> = OnItemBindClass<OptionAttribute>().map(
        OptionAttribute::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.optionKey, R.layout.option_item)
    }

    fun placeOrder(
        token: String,
        shippingBuildingNo: String,
        shippingZone: String,
        shippingStreet: String,
        code: String
    ) {
        mainScope.launch {
            loading.postValue(true)
            val response =
                repository.placeOrder(token, shippingBuildingNo, shippingZone, shippingStreet,code)

            loading.postValue(false)

            response?.let {
                placeOrderResponse.value = it
            }
        }
    }

    fun getOrderList(token: String) {
        mainScope.launch {
            loading.postValue(true)
            val response =
                repository.getOrderList(token)

            loading.postValue(false)

            response?.response?.orders?.let {
                orderHistoryList.addAll(it)
            }
        }
    }

    fun validateAddress(): Boolean {
        when {
            street.value.isNullOrEmpty() -> streetError.value = "Street address is empty"
            zone.value.isNullOrEmpty() -> zoneError.value = "Zone is empty"
            building.value.isNullOrEmpty() -> buildingError.value = "Building number is empty"
            else -> return true
        }
        return false
    }
}