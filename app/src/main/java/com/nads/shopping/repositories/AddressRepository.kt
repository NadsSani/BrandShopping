package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.Address
import com.nads.shopping.datamodels.BaseModel
import com.nads.shopping.datamodels.BasicResponse

class AddressRepository(private val api: EndPoints) : BaseRepository() {

    suspend fun getAddressList(token: String): BaseModel<List<Address>>? {
        return safeApiCall(
            call = { api.getAddressListAsync(token).await() },
            errorMessage = "Error in getting address list"
        )
    }

    suspend fun addAddress(
        token: String,
        addressName: String,
        shippingBuildingNo: String,
        shippingZone: String,
        shippingStreet: String
    ): BaseModel<List<Address>>? {
        return safeApiCall(
            call = { api.addAddressAsync(token, addressName, shippingBuildingNo, shippingZone, shippingStreet).await() },
            errorMessage = "Error in getting address list"
        )
    }

    suspend fun editAddress(
        token: String,
        addressName: String,
        shippingBuildingNo: String,
        shippingZone: String,
        shippingStreet: String,
        addressId: String
    ): BasicResponse? {
        return safeApiCall(
            call = { api.editAddressAsync(token, addressName, shippingBuildingNo, shippingZone, shippingStreet,addressId).await() },
            errorMessage = "Error updating address"
        )
    }


}