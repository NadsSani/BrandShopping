package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.*

class CartRepository(private val api: EndPoints) : BaseRepository() {
    suspend fun addCart(token: String,productId: String, quantity: String, inventoryId: String): BasicResponse? {

        return safeApiCall(
            call = { api.addCartAsync(token,productId, quantity, inventoryId).await() },
            errorMessage = "Error in add to cart"
        )
    }

    suspend fun getCartCount(token: String): CartCountResponse? {

        return safeApiCall(
            call = { api.getCartCountAsync(token).await() },
            errorMessage = "Error get cart count"
        )
    }

    suspend fun getCartList(token: String): BaseModel<CartListResponse>? {

        return safeApiCall(
            call = { api.getCartListAsync(token).await() },
            errorMessage = "Error get cart list"
        )
    }

    suspend fun removeCartItem(token: String, cartId: String): BasicResponse? {

        return safeApiCall(
            call = { api.removeCartItemAsync(token, cartId).await() },
            errorMessage = "Error remove from cart list"
        )
    }

    suspend fun updateCart(token: String, cartId: String, quantity: String): BasicResponse? {

        return safeApiCall(
            call = { api.updateCartAsync(token, cartId, quantity).await() },
            errorMessage = "Error updating cart quanity"
        )
    }

}