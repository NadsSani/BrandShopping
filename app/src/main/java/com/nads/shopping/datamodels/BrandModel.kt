package com.nads.shopping.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Brand(
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("name_ar")
    @Expose
    val nameAr: String = "",
    @SerializedName("details")
    @Expose
    val details: String = "",
    @SerializedName("details_ar")
    @Expose
    val detailsAr: String = "",
    @SerializedName("photo")
    @Expose
    val photo: String = "",
    var isSelected: Boolean = false,
    )