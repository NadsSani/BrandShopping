package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.OrderResponseSuccess
import com.nads.shopping.datamodels.PlaceOrderResponse

class OrderRepository(private val api: EndPoints) : BaseRepository() {

    suspend fun placeOrder(
        token: String,
        shippingBuildingNo: String,
        shippingZone: String,
        shippingStreet: String,
        code: String
    ): PlaceOrderResponse? {

        return safeApiCall(
            call = {
                api.placeOrderAsync(
                    token,
                    shippingBuildingNo,
                    shippingZone,
                    shippingStreet,
                    code
                ).await()
            },
            errorMessage = "Error get cart list"
        )
    }

    suspend fun getOrderList(token: String): OrderResponseSuccess? {

        return safeApiCall(
            call = { api.getOrderHistoryAsync(token).await() },
            errorMessage = "Error get order list"
        )
    }

}