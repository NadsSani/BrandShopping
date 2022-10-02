package com.nads.shopping.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TimeSlotModel(
    @SerializedName("time")
    @Expose
    val time: String = "",
    @SerializedName("status")
    @Expose
    val status: Int = 0,
    var isSelected: Boolean = false,
)