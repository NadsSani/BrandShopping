package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.*

class FavoritesRepository(private val api: EndPoints) : BaseRepository() {
    suspend fun addToFavorites(token: String,productId: String,isFavorite: Int): BasicResponse? {

        return safeApiCall(
            call = { api.addToFavoritesAsync(token, productId, isFavorite).await() },
            errorMessage = "Error in loading banners"
        )
    }
    suspend fun getFavoritesAsync(token: String): BaseModel<List<FavoritesItem>>? {

        return safeApiCall(
            call = { api.getFavoritesAsync(token).await() },
            errorMessage = "Error in loading banners"
        )
    }

}