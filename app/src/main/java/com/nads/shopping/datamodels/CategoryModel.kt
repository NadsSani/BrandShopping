package com.nads.shopping.datamodels

import androidx.databinding.ObservableArrayList
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("name_ar")
    @Expose
    val nameAr: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("slug")
    @Expose
    val slug: String = "",
    @SerializedName("photo")
    @Expose
    var photo: String = "",
    @SerializedName("sub_cat_count")
    @Expose
    val subCategoryCount:  Int=0,
    @SerializedName("sub_cat")
    @Expose
    val subCategories:ObservableArrayList<SubCategory>,
    var isSelected: Boolean = false,

)

data class SubCategory(
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("name_ar")
    @Expose
    val nameAr: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("slug")
    @Expose
    val slug: String = "",
    @SerializedName("photo")
    @Expose
    var photo: String = "",
    @SerializedName("child_cat_count")
    @Expose
    val childCategoryCount: Int=0,
    var isSelected: Boolean = false,

)