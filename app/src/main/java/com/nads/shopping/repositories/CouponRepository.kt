package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.*

class CouponRepository(private val api: EndPoints) : BaseRepository() {


    suspend fun getCoupons(): BaseModel<List<Coupon>>? {
        return safeApiCall(
            call = { api.getCouponsAsync().await() },
            errorMessage = "Error in getting coupon list"
        )
    }

    suspend fun applyCoupons( token: String,
                             code: String): BaseModel<CouponPriceDetails>? {
        return safeApiCall(
            call = { api.applyCouponAsync(token, code).await() },
            errorMessage = "Error in apply coupon "
        )
    }


}