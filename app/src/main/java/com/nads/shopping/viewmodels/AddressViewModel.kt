package com.nads.shopping.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.BR
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.Address
import com.nads.shopping.datamodels.BaseModel
import com.nads.shopping.datamodels.BasicResponse
import com.nads.shopping.listeners.ItemSelectListener
import com.nads.shopping.repositories.AddressRepository
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class AddressViewModel : BaseViewModel() {
    val loading = MutableLiveData<Boolean>()

    val addressList = ObservableArrayList<Address>()
    val submitButtonText = MutableLiveData<Int>()
    val name = MutableLiveData<String>()
    val street = MutableLiveData<String>()
    val zone = MutableLiveData<String>()
    val building = MutableLiveData<String>()

    val nameError = MutableLiveData<Int>()
    val streetError = MutableLiveData<Int>()
    val zoneError = MutableLiveData<Int>()
    val buildingError = MutableLiveData<Int>()

    private val addAddressResponse = LiveEvent<BaseModel<List<Address>>>()
    val addAddressResponseEvent = addAddressResponse

    private val editAddressResponse = LiveEvent<BasicResponse>()
    val editAddressResponseEvent = editAddressResponse

    private val addressSelect = LiveEvent<Address>()
    val addressSelectEvent = addressSelect

    private val repository = AddressRepository(ApiFactory.brandattyAPI)

    val addressSelectListener = object : ItemSelectListener<Address> {
        override fun onItemSelected(item: Address) {
            addressSelect.value = item
        }

    }

    val addressItemBinding: OnItemBindClass<Address> = OnItemBindClass<Address>().map(
        Address::class.java
    ) { itemBinding, position, item ->
        itemBinding.set(BR.addressItem, R.layout.address_item)

        itemBinding.bindExtra(BR.listener, addressSelectListener)
    }

    init {
        submitButtonText.value = R.string.create
    }

    fun getAddressList(token: String) {
        mainScope.launch {
            loading.postValue(true)
            val response =
                repository.getAddressList(token)

            loading.postValue(false)

            response?.data?.let {
                addressList.clear()
                addressList.addAll(it)
            }
        }
    }

    fun addAddress(token: String) {
        if (validateAddress())
            mainScope.launch {
                loading.postValue(true)
                val response =
                    repository.addAddress(
                        token,
                        name.value.toString(),
                        building.value.toString(),
                        zone.value.toString(),
                        street.value.toString()
                    )

                loading.postValue(false)

                response?.let {
                    addAddressResponse.value = it
                }
            }
    }

    fun editAddress(token: String, addressId: String) {
        if (validateAddress())
            mainScope.launch {
                loading.postValue(true)
                val response =
                    repository.editAddress(
                        token,
                        name.value.toString(),
                        building.value.toString(),
                        zone.value.toString(),
                        street.value.toString(),
                        addressId
                    )

                loading.postValue(false)

                response?.let {
                    editAddressResponse.value = it
                }
            }
    }

    private fun validateAddress(): Boolean {
        when {
            name.value.isNullOrEmpty() -> nameError.value = R.string.empty_name
            street.value.isNullOrEmpty() -> streetError.value = R.string.empty_street
            zone.value.isNullOrEmpty() -> zoneError.value = R.string.empty_zone
            building.value.isNullOrEmpty() -> buildingError.value = R.string.empty_building
            else -> return true
        }
        return false
    }
}