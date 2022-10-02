package com.nads.shopping.datamodels

import androidx.databinding.ObservableArrayList
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("order_number")
    @Expose
    val orderNumber: String,
    @SerializedName("Date")
    @Expose
    val date: String,
    @SerializedName("totalAmount")
    @Expose
    val totalAmount: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("name_ar")
    @Expose
    val nameAr: String,
    @SerializedName("brand_name")
    @Expose
    val brandName: String,
    @SerializedName("brand_name_ar")
    @Expose
    val brandNameAr: String,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("quantity")
    @Expose
    val quantity: String,
    @SerializedName("price")
    @Expose
    val price: String,
    @SerializedName("total")
    @Expose
    val total: String,
    @SerializedName("attributes")
    @Expose
    val attributes: ObservableArrayList<OptionAttribute>,
)

data class OrderResponseSuccess(
    @SerializedName("success")
    @Expose
    val response: OrderListResponse,
)

data class OrderListResponse(
    @SerializedName("status")
    @Expose
    val status:Int=0,
    @SerializedName("message")
    @Expose
    val message:String,
    @SerializedName("data")
    @Expose
    val orders:ObservableArrayList<Order>,
)
