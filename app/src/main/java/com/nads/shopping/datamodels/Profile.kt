package com.nads.shopping.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("email")
    @Expose
    var email: String = "",
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("phone")
    @Expose
    var phone: String = "",
    @SerializedName("dob")
    @Expose
    var dob: String = "",
    @SerializedName("gender")
    @Expose
    var gender: String = "",
    @SerializedName("photo")
    @Expose
    var photo: String = "",
)
