package com.nads.shopping.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BasicResponse (
    @SerializedName("status")
    @Expose
    val status: Int = 0,
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("message_ar")
    @Expose
    val messageAr: String = "",
)