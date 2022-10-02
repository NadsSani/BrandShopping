package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.BaseModel
import com.nads.shopping.datamodels.Category
import com.nads.shopping.datamodels.SubCategory

class CategoryRepository(private val api: EndPoints) : BaseRepository() {
    suspend fun getCategories(): BaseModel<List<Category>>? {

        return safeApiCall(
            call = { api.getCategoriesAsync().await() },
            errorMessage = "Error in loading categories"
        )
    }

    suspend fun getSubCategories(categoryId: String): BaseModel<List<SubCategory>>? {

        return safeApiCall(
            call = { api.getSubCategoriesAsync(categoryId).await() },
            errorMessage = "Error in loading sub categories"
        )
    }


/*    suspend fun getProfile(token: String): LoginResponse? {

        return safeApiCall(
            call = { api.getProfileAsync(token).await() },
            errorMessage = "Something went wrong"
        )
    }

    suspend fun updateProfile(
        token: String,
        name: String,
        email: String,
        photo: MultipartBody.Part?
    ): LoginResponse? {

        return safeApiCall(
            call = { api.updateProfileAsync(token, name.createRequestBody(), email.createRequestBody(), photo).await() },
            errorMessage = "Something went wrong"
        )
    }*/
}