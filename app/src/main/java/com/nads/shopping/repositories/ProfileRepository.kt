package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.BaseModel
import com.nads.shopping.datamodels.Profile
import com.nads.shopping.utils.createRequestBody
import okhttp3.MultipartBody

class ProfileRepository(private val api: EndPoints) : BaseRepository() {
    suspend fun getBasicInfo(token: String): BaseModel<Profile>? {

        return safeApiCall(
            call = { api.getBasicInfoAsync(token).await() },
            errorMessage = "Error in getting basic info"
        )
    }

    suspend fun updateBasicInfo(
        token: String,
        name: String,
        email: String,
        phone: String,
        dob: String,
        gender: String,
        photo: MultipartBody.Part?
    ): BaseModel<Profile>? {

        return safeApiCall(
            call = {
                api.updateBasicInfoAsync(
                    token,
                    name.createRequestBody(),
                    email.createRequestBody(),
                    phone.createRequestBody(),
                    dob.createRequestBody(),
                    gender.createRequestBody(),
                    photo
                ).await()
            },
            errorMessage = "Error in updating basic info"
        )
    }


}