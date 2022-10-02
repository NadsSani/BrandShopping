package com.nads.shopping.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("token")
    @Expose
    val token: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("phone")
    @Expose
    val phone: String = "",
    @SerializedName("email")
    @Expose
    var email: String = "",
    @SerializedName("photo")
    @Expose
    val photo: String = "",
)