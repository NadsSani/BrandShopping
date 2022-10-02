package com.nads.shopping.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Coupon (
    @SerializedName("code")
    @Expose
    val code: String = "",
    @SerializedName("details")
    @Expose
    val details: String = "",
    @SerializedName("details_ar")
    @Expose
    val detailsAr: String = "",
)