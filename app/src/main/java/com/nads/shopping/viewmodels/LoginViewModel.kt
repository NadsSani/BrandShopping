package com.nads.shopping.viewmodels

import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.BaseModel
import com.nads.shopping.datamodels.BasicResponse
import com.nads.shopping.datamodels.LoginResponse
import com.nads.shopping.repositories.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    val loading = MutableLiveData<Boolean>()
    val resetPasswordLoading = MutableLiveData<Boolean>()
    val phone = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val resetPasswordEmail = MutableLiveData<String>()
    val resetPasswordEmailError = MutableLiveData<Int>()

    val phoneError = MutableLiveData<Int>()
    val passwordError = MutableLiveData<Int>()

    val loginResponse = MutableLiveData<BaseModel<LoginResponse>>()
   private val resetPasswordResponse = LiveEvent<BasicResponse>()
    val resetPasswordResponseEvent = resetPasswordResponse

    val repository = UserRepository(ApiFactory.brandattyAPI)

    init {
//        phone.value = "31404151"
//        password.value = "123456"
    }

    fun doLogin() {
        scope.launch {
            loading.postValue(true)
            val response = if (validate()) repository.doLogin(
                phone.value.toString(),
                password.value.toString()
            ) else null

            loading.postValue(false)

            response?.let {
                loginResponse.postValue(it)
            }
        }
    }

    fun forgotPassword() {
        scope.launch {
            resetPasswordLoading.postValue(true)
            val response = if (validateResetPassword()) repository.forgotPassword(
                resetPasswordEmail.value.toString(),
            ) else null

            resetPasswordLoading.postValue(false)

            response?.let {
                resetPasswordResponse.postValue(it)
            }
        }
    }

    private fun validate(): Boolean {
        when {
            phone.value.isNullOrEmpty() -> phoneError.postValue(R.string.empty_phone)
            password.value.isNullOrEmpty() -> passwordError.postValue(R.string.empty_password)
            else -> return true
        }
        return false
    }

    private fun validateResetPassword(): Boolean {
        when {
            resetPasswordEmail.value.isNullOrEmpty() -> resetPasswordEmailError.postValue(R.string.empty_email)
            else -> return true
        }
        return false
    }
}