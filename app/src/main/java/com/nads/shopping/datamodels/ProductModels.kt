package com.nads.shopping.datamodels

import androidx.databinding.ObservableArrayList
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductListResponse(
    @SerializedName("status")
    @Expose
    val status: Int = 0,
    @SerializedName("total")
    @Expose
    val total: Int = 0,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("message_ar")
    @Expose
    val messageAr: String,
    @SerializedName("data")
    @Expose
    val products: List<Product>,
)

data class Product(
    @SerializedName("products_id")
    @Expose
    val productsId: String,
    @SerializedName("brand_name")
    @Expose
    val brandName: String,
    @SerializedName("brand_name_ar")
    @Expose
    val brandNameAr: String,
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
    @SerializedName("wish_list")
    @Expose
    val isFavorite: Int = 0,
    val showPreviousPrice: Boolean = previousPrice != "0" && previousPrice != null

)

data class ProductDetail(
    @SerializedName("post_id")
    @Expose
    val productId: String,
    @SerializedName("product_rating")
    @Expose
    val productRating: String,
    @SerializedName("sku")
    @Expose
    val sku: String,
    @SerializedName("views")
    @Expose
    val views: String,
    @SerializedName("brand_name")
    @Expose
    val brandName: String,
    @SerializedName("brand_name_ar")
    @Expose
    val brandNameAr: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("name_ar")
    @Expose
    val nameAr: String,
    @SerializedName("category_id")
    @Expose
    val categoryId: String,
    @SerializedName("subcategory_id")
    @Expose
    val subCategoryId: String,
    @SerializedName("childcategory_id")
    @Expose
    val childCategoryId: String,
    @SerializedName("details")
    @Expose
    val details: String,
    @SerializedName("details_ar")
    @Expose
    val detailsAr: String,
    @SerializedName("delivery_info")
    @Expose
    val deliveryInfo: String,
    @SerializedName("delivery_info_ar")
    @Expose
    val deliveryInfoAr: String,
    @SerializedName("policy")
    @Expose
    val policy: String,
    @SerializedName("policy_ar")
    @Expose
    val policyAr: String,
    @SerializedName("photo")
    @Expose
    val photo: String,
    @SerializedName("previous_price")
    @Expose
    var previousPrice: String,
    @SerializedName("seller_name")
    @Expose
    val sellerName: String,
    @SerializedName("price")
    @Expose
    var price: String,
    @SerializedName("inventory_id")
    @Expose
    var inventoryId: String,
    @SerializedName("selected_option")
    @Expose
    val selectedOptions: String,
    @SerializedName("wish_list")
    @Expose
    var isFavorite: Int = 0,
    @SerializedName("stock")
    @Expose
    var stock: Int = 0,
    @SerializedName("stock_ary")
    @Expose
    val options: ObservableArrayList<Option>,
    @SerializedName("gallery")
    @Expose
    val gallery: ObservableArrayList<String>,
)

data class Option(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("name_ar")
    @Expose
    val nameAr: String,
    @SerializedName("keys")
    @Expose
    val keys: ObservableArrayList<OptionKey>,
)

data class OptionKey(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("option_id")
    @Expose
    val optionId: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("name_ar")
    @Expose
    val nameAr: String,
    @SerializedName("image")
    @Expose
    val photo: String,
    var isSelected:Boolean=false
)

data class InventoryResponse (
    @SerializedName("status")
    @Expose
    val status: Int = 0,
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("message_ar")
    @Expose
    val messageAr: String = "",
    @SerializedName("inventory")
    @Expose
    val inventory:Inventory
)

data class Inventory (
    @SerializedName("stock")
    @Expose
    val stock: Int = 0,
    @SerializedName("price")
    @Expose
    val price: String = "",
    @SerializedName("previous_price")
    @Expose
    val previousPrice: String = "",
    @SerializedName("inventory_id")
    @Expose
    val inventoryId: String = ""
)