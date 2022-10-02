package com.nads.shopping.viewmodels

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.BR
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.*
import com.nads.shopping.listeners.CartListener
import com.nads.shopping.repositories.CartRepository
import com.nads.shopping.repositories.CouponRepository
import com.nads.shopping.utils.getToken
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class CartViewModel : BaseViewModel() {
    val loading = MutableLiveData<Boolean>()
    val couponCode = MutableLiveData<String>()
    val cartListResponse = MutableLiveData<CartListResponse>()

    private val applyCouponResponse = LiveEvent<BaseModel<CouponPriceDetails>>()
    val applyCouponResponseEvent = applyCouponResponse

    val cartList = ObservableArrayList<CartItem>()
    var lastSelectedCartItem = 0

    private val cartItemSelect = LiveEvent<CartItem>()
    val cartItemSelectEvent = cartItemSelect

//    private val removeCartItemResponse = LiveEvent<BasicResponse>()
//    val removeCartItemResponseEvent = removeCartItemResponse

    private val addCartResponse = LiveEvent<BasicResponse>()
    val addCartResponseEvent = addCartResponse

    val repository = CartRepository(ApiFactory.brandattyAPI)
    val couponRepository = CouponRepository(ApiFactory.brandattyAPI)

    private val cartListener = object : CartListener {
        override fun onQuantityPlus(view: View, item: CartItem) {
            val quantity: Int = item.quantity.toInt()
            updateCart(view.context.getToken(), item.cartId, (quantity + 1).toString())
        }

        override fun onQuantityMinus(view: View, item: CartItem) {
            val quantity: Int = item.quantity.toInt()
            updateCart(view.context.getToken(), item.cartId, (quantity - 1).toString())
        }

        override fun onItemSelected(item: CartItem) {

        }

        override fun onRemoveSelected(view: View, item: CartItem) {
            removeCartItem(view.context.getToken(), item.cartId)
        }
    }

    init {
    }

    val optionsKeyBinding: OnItemBindClass<OptionAttribute> =
        OnItemBindClass<OptionAttribute>().map(
            OptionAttribute::class.java
        ) { itemBinding, position, item ->
            itemBinding.set(BR.optionKey, R.layout.option_item)
        }

    val cartItemBinding: OnItemBindClass<CartItem> = OnItemBindClass<CartItem>().map(
        CartItem::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.cartItem, R.layout.cart_item)

        itemBinding.bindExtra(BR.viewModel, this)
        itemBinding.bindExtra(BR.listener, cartListener)
    }

    val summaryItemBinding: OnItemBindClass<CartItem> = OnItemBindClass<CartItem>().map(
        CartItem::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.summaryItem, R.layout.summary_item)

        itemBinding.bindExtra(BR.viewModel, this)
    }

    fun addCart(token: String, productId: String, quantity: String, inventoryId: String) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.addCart(token, productId, quantity, inventoryId)

            loading.postValue(false)

            response?.let {
                addCartResponse.value = it
            }
        }
    }

    fun getCartList(token: String) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getCartList(token)

            loading.postValue(false)

            response?.data?.let {
                cartListResponse.value = it
            }
        }
    }

    fun updateCart(token: String, cartId: String, quantity: String) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.updateCart(token, cartId, quantity)

            loading.postValue(false)

            response?.let {
                getCartList(token)
            }
        }
    }

    fun removeCartItem(token: String, cartId: String) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.removeCartItem(token, cartId)

            loading.postValue(false)

            response?.let {
                getCartList(token)
            }
        }
    }

    fun applyCoupon(token: String,) {
        mainScope.launch {
            loading.postValue(true)
            val response = couponRepository.applyCoupons(token, couponCode.value.toString())

            loading.postValue(false)

            response?.let {
                applyCouponResponse.postValue(it)
            }
        }
    }
}