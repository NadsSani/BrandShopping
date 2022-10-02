package com.nads.shopping.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductSize(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("photo")
    @Expose
    val name: String,
    var isSelected:Boolean=false
)