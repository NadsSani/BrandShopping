package com.nads.shopping.datamodels

import androidx.databinding.ObservableArrayList
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("cart_id")
    @Expose
    val cartId: String,
    @SerializedName("product_id")
    @Expose
    val productId: String,
    @SerializedName("product_name")
    @Expose
    val productName: String,
    @SerializedName("brand_name")
    @Expose
    val brandName: String,
    @SerializedName("brand_name_ar")
    @Expose
    val brandNameAr: String,
    @SerializedName("product_name_ar")
    @Expose
    val productNameAr: String,
    @SerializedName("product_photo")
    @Expose
    val productPhoto: String,
    @SerializedName("quantity")
    @Expose
    val quantity: String,
    @SerializedName("unit_price")
    @Expose
    val unitPrice: String,
    @SerializedName("sub_total")
    @Expose
    val subTotal: String,
    @SerializedName("attributes")
    @Expose
    val attributes: ObservableArrayList<OptionAttribute>,
    var isSelected: Boolean = false
)

data class CartCountResponse(
    @SerializedName("status")
    @Expose
    val status: Int = 0,
    @SerializedName("count")
    @Expose
    val count: Int = 0
)

data class CartListResponse(
    @SerializedName("cart_list")
    @Expose
    val cartList: ObservableArrayList<CartItem> = ObservableArrayList(),
    @SerializedName("price_details")
    @Expose
    val priceDetails:PriceDetails,
)

data class PriceDetails(
    @SerializedName("discount")
    @Expose
    var discount: String = "",
    @SerializedName("total")
    @Expose
    var total: String = "",
    @SerializedName("sub_total")
    @Expose
    var subTotal: String = "",
    @SerializedName("delivery_price")
    @Expose
    var deliveryPrice: String = "",
)

data class OptionAttribute(
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("name_ar")
    @Expose
    val nameAr: String = "",
    @SerializedName("image")
    @Expose
    val image: String = "",
)

data class CouponPriceDetails(
    @SerializedName("price_details")
    @Expose
    val priceDetails: PriceDetails
)