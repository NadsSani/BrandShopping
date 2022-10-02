package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.Banner
import com.nads.shopping.datamodels.BaseModel

class BannerRepository(private val api: EndPoints) : BaseRepository() {
    suspend fun getBanners(): BaseModel<List<Banner>>? {

        return safeApiCall(
            call = { api.getBannersAsync().await() },
            errorMessage = "Error in loading banners"
        )
    }

}