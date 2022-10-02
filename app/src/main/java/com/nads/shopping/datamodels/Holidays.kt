package com.nads.shopping.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Holidays(
    @SerializedName("id")
    @Expose
    val id:Int = 0,
    @SerializedName("date")
    @Expose
    val date:String = "")
