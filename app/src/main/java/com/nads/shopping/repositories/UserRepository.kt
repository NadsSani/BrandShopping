package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.BaseModel
import com.nads.shopping.datamodels.BasicResponse
import com.nads.shopping.datamodels.LoginResponse
import com.nads.shopping.datamodels.SignUpResponse

class UserRepository(private val api: EndPoints) : BaseRepository() {
    suspend fun doLogin(phone: String, password: String): BaseModel<LoginResponse>? {

        return safeApiCall(
            call = { api.doLoginAsync(phone, password).await() },
            errorMessage = "Error in Login"
        )
    }

    suspend fun doSigUp(name: String,email: String,phone: String, password: String): SignUpResponse? {

        return safeApiCall(
            call = { api.doSignUpAsync(name, email, password, phone).await() },
            errorMessage = "Error in Login"
        )
    }

    suspend fun forgotPassword(email: String): BasicResponse? {

        return safeApiCall(
            call = { api.forgotPasswordAsync(email).await() },
            errorMessage = "Error in reset password"
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