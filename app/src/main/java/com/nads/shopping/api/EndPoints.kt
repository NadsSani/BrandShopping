package com.nads.shopping.api

import com.nads.shopping.datamodels.*
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface EndPoints {
    @FormUrlEncoded
    @POST("login")
    fun doLoginAsync(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Deferred<Response<BaseModel<LoginResponse>>>

    @FormUrlEncoded
    @POST("signUp")
    fun doSignUpAsync(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String
    ): Deferred<Response<SignUpResponse>>

    @POST("getCategories")
    fun getCategoriesAsync(): Deferred<Response<BaseModel<List<Category>>>>

    @POST("getBanners")
    fun getBannersAsync(): Deferred<Response<BaseModel<List<Banner>>>>

    @POST("getSubCategories")
    fun getSubCategoriesAsync(@Query("category_id") categoryId: String): Deferred<Response<BaseModel<List<SubCategory>>>>

    @POST("list_products")
    fun getProductListAsync(
        @Query("main_category") categoryId: String="",
        @Query("sub_category") subCategoryId: String="",
        @Query("child_category") childCategoryId: String="",
        @Query("price_filter") priceFilter: String="",
        @Query("shop_id") shopId: String="",
        @Query("page") page: Int = 1,
        @Query("new_collection") newCollection: Int?=null,
        @Query("best_sellers") bestSellers: Int?=null,
        @Query("keywords") keywords: String="",
        @Query("brands_id") brandId: String?=null,
    ): Deferred<Response<ProductListResponse>>

//    @POST("list_products")
//    fun getProductListAsyncs(
//        @Query("main_category") categoryId: String="",
//        @Query("sub_category") subCategoryId: String="",
//        @Query("child_category") childCategoryId: String="",
//        @Query("price_filter") priceFilter: String="",
//        @Query("shop_id") shopId: String="",
//        @Query("page") page: Int = 1,
//        @Query("new_collection") newCollection: Int?=null,
//        @Query("best_sellers") bestSellers: Int?=null,
//        @Query("keywords") keywords: String="",
//        @Query("brands_id") brandId: String?=null,
//    ): Flow<Response<ProductListResponse>>



    @POST("products_details")
    fun getProductDetailsAsync(@Query("post_id") productId: String): Deferred<Response<BaseModel<ProductDetail>>>

    @POST("savePost")
    fun addToFavoritesAsync(
        @Header("token") token: String,
        @Query("post_id") productId: String,
        @Query("wish_list") isFavorite: Int
    ): Deferred<Response<BasicResponse>>

    @POST("savePostList")
    fun getFavoritesAsync(
        @Header("token") token: String,
    ): Deferred<Response<BaseModel<List<FavoritesItem>>>>

    @FormUrlEncoded
    @POST("addCart")
    fun addCartAsync(
        @Header("token") token: String,
        @Field("product_id") productId: String,
        @Field("quantity") quantity: String,
        @Field("inventory_id") inventoryId: String,
    ): Deferred<Response<BasicResponse>>

    @POST("countCart")
    fun getCartCountAsync(
        @Header("token") token: String,
    ): Deferred<Response<CartCountResponse>>

    @POST("listCart")
    fun getCartListAsync(
        @Header("token") token: String,
    ): Deferred<Response<BaseModel<CartListResponse>>>

    @FormUrlEncoded
    @POST("removeCart")
    fun removeCartItemAsync(
        @Header("token") token: String,
        @Field("cart_id") cartId: String,
    ): Deferred<Response<BasicResponse>>

    @FormUrlEncoded
    @POST("placeOrder")
    fun placeOrderAsync(
        @Header("token") token: String,
        @Field("shipping_building_no") shippingBuildingNo: String,
        @Field("shipping_zone") shippingZone: String,
        @Field("shipping_street") shippingStreet: String,
        @Field("code") code: String,
    ): Deferred<Response<PlaceOrderResponse>>

    @FormUrlEncoded
    @POST("getInventory")
    fun getInventoryAsync(
        @Field("product_id") productId: String,
        @Field("option") options: String,
    ): Deferred<Response<InventoryResponse>>

    @POST("getBasicInfo")
    fun getBasicInfoAsync(
        @Header("token") token: String,
    ): Deferred<Response<BaseModel<Profile>>>

    @POST("orderHistory")
    fun getOrderHistoryAsync(
        @Header("token") token: String,
    ): Deferred<Response<OrderResponseSuccess>>

    @POST("listShippingAddress")
    fun getAddressListAsync(
        @Header("token") token: String,
    ): Deferred<Response<BaseModel<List<Address>>>>

    @Multipart
    @POST("updateBasicInfo")
    fun updateBasicInfoAsync(
        @Header("token") token: String,
        @Part("name") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("dob") dob: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part photo: MultipartBody.Part? = null
    ): Deferred<Response<BaseModel<Profile>>>

    @FormUrlEncoded
    @POST("addShippingAddress")
    fun addAddressAsync(
        @Header("token") token: String,
        @Field("addres_name") addressName: String,
        @Field("building_number") shippingBuildingNo: String,
        @Field("zone_number") shippingZone: String,
        @Field("street_number") shippingStreet: String,
    ): Deferred<Response<BaseModel<List<Address>>>>

    @FormUrlEncoded
    @POST("editShippingAddress")
    fun editAddressAsync(
        @Header("token") token: String,
        @Field("addres_name") addressName: String,
        @Field("building_number") shippingBuildingNo: String,
        @Field("zone_number") shippingZone: String,
        @Field("street_number") shippingStreet: String,
        @Field("address_id") addressId: String,
    ): Deferred<Response<BasicResponse>>

    @FormUrlEncoded
    @POST("changePassword")
    fun changePasswordAsync(
        @Header("token") token: String,
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String,
        @Field("confirmPassword") confirmPassword: String,
    ): Deferred<Response<BasicResponse>>

    @FormUrlEncoded
    @POST("updateCartQuantity")
    fun updateCartAsync(
        @Header("token") token: String,
        @Field("cart_id") cartId: String,
        @Field("quantity") quantity: String,
    ): Deferred<Response<BasicResponse>>

    @FormUrlEncoded
    @POST("forgot")
    fun forgotPasswordAsync(
        @Field("email") email: String,
    ): Deferred<Response<BasicResponse>>

    @FormUrlEncoded
    @POST("applyCoupon")
    fun applyCouponAsync(
        @Header("token") token: String,
        @Field("code") code: String,
    ): Deferred<Response<BaseModel<CouponPriceDetails>>>

    @POST("getBrands")
    fun getBrandsAsync(): Deferred<Response<BaseModel<List<Brand>>>>

    @FormUrlEncoded
    @POST("timeSlot")
    fun getTimeSlotsAsync( @Header("token") token: String,
                           @Field("type") type:String,
                          @Field("date") date: String):
            Deferred<Response<BaseModel<List<TimeSlotModel>>>>

    @FormUrlEncoded
    @POST("bus_holidays")
    fun getbusholidaysAsync(
                            @Field("type") type:Int):
                            Deferred<Response<BaseModel<List<Holidays>>>>

    @POST("coupon")
    fun getCouponsAsync(): Deferred<Response<BaseModel<List<Coupon>>>>


    @FormUrlEncoded
    @POST("book_bus")
    fun book_busAsync(
        @Header("token") token: String,
        @Field("type") type: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("building") building: String,
        @Field("street") street: String,
        @Field("long") long: String,
        @Field("zone") zone: String,
        @Field("land_mark") land_mark: String,
        @Field("date") date: String,
        @Field("time") time: String,
        @Field("map_address") map_address: String,
        @Field("lat") lat: String,
        @Field("category_id") category_id: String,
    ):Deferred<Response<BasicModel>>


}