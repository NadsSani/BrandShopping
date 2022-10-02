package com.nads.shopping.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlaceOrderResponse (
    @SerializedName("success")
    @Expose
    val success: BaseModel<OrderResponse>,
)
data class OrderResponse (
    @SerializedName("order_id")
    @Expose
    val orderId: String = "",
)