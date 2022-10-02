package com.nads.shopping.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FavoritesItem(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("post_id")
    @Expose
    val postId: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("name_ar")
    @Expose
    val nameAr: String,
    @SerializedName("photo")
    @Expose
    val photo: String,
    @SerializedName("price")
    @Expose
    val price: String,
    @SerializedName("previous_price")
    @Expose
    val previousPrice: String,
    @SerializedName("inventory_id")
    @Expose
    val inventoryId: String,
    @SerializedName("brand_name")
    @Expose
    val brandName: String,
    @SerializedName("brand_name_ar")
    @Expose
    val brandNameAr: String,
    val showPreviousPrice: Boolean = previousPrice != "0" && previousPrice != null
)