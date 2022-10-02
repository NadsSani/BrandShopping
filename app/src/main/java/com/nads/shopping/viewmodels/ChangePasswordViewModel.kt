package com.nads.shopping.viewmodels

import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.BasicResponse
import com.nads.shopping.repositories.ChangePasswordRepository
import kotlinx.coroutines.launch

class ChangePasswordViewModel : BaseViewModel() {

    val loading = MutableLiveData<Boolean>()
    val oldPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val confirmNewPassword = MutableLiveData<String>()

    val oldPasswordError = MutableLiveData<Int>()
    val newPasswordError = MutableLiveData<Int>()
    val confirmNewPasswordError = MutableLiveData<Int>()

    val changePasswordResponse = LiveEvent<BasicResponse>()
    val changePasswordEvent = changePasswordResponse

    val repository = ChangePasswordRepository(ApiFactory.brandattyAPI)

    init {

    }

    fun changePassword(token: String) {
        if (validate())
            scope.launch {
                loading.postValue(true)
                val response = if (validate()) repository.changePassword(
                    token,
                    oldPassword.value.toString(),
                    newPassword.value.toString(),
                    confirmNewPassword.value.toString()
                ) else null

                loading.postValue(false)

                response?.let {
                    changePasswordResponse.postValue(it)
                }
            }
    }

    fun validate(): Boolean {
        when {
            oldPassword.value.isNullOrEmpty() -> oldPasswordError.postValue(R.string.empty_old_password)
            newPassword.value.isNullOrEmpty() -> newPasswordError.postValue(R.string.empty_new_password)
            confirmNewPassword.value.isNullOrEmpty() -> confirmNewPasswordError.postValue(R.string.empty_confirm_password)
            confirmNewPassword.value != newPassword.value -> {
                confirmNewPasswordError.postValue(R.string.confirm_password_mismatch)
                newPasswordError.postValue(R.string.confirm_password_mismatch)
            }
            else -> return true
        }
        return false
    }
}