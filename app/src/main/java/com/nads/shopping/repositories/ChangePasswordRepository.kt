package com.nads.shopping.repositories

import com.nads.shopping.api.EndPoints
import com.nads.shopping.datamodels.BasicResponse

class ChangePasswordRepository(private val api: EndPoints) : BaseRepository() {

    suspend fun changePassword(
        token: String,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): BasicResponse? {
        return safeApiCall(
            call = {
                api.changePasswordAsync(token, oldPassword, newPassword, confirmPassword).await()
            },
            errorMessage = "Error in change password"
        )
    }

}