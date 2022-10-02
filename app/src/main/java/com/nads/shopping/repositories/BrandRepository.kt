package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.BaseModel
import com.nads.shopping.datamodels.Brand

class BrandRepository(private val api: EndPoints) : BaseRepository() {


    suspend fun getBrands(): BaseModel<List<Brand>>? {
        return safeApiCall(
            call = { api.getBrandsAsync().await() },
            errorMessage = "Error in getting brand list"
        )
    }


}