package com.nads.shopping.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Address (
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("addres_name")
    @Expose
    val addressName: String = "",
    @SerializedName("building_number")
    @Expose
    val buildingNumber: String = "",
    @SerializedName("zone_number")
    @Expose
    val zoneNumber: String = "",
    @SerializedName("street_number")
    @Expose
    val streetNumber: String = "",
)