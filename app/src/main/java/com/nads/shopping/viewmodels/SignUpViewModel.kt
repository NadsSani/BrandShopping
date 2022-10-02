package com.nads.shopping.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nads.shopping.R
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.SignUpResponse
import com.nads.shopping.repositories.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel : BaseViewModel() {

    val loading = MutableLiveData<Boolean>()
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val nameError = MutableLiveData<Int>()
    val emailError = MutableLiveData<Int>()
    val phoneError = MutableLiveData<Int>()
    val passwordError = MutableLiveData<Int>()

    val loginResponse = MutableLiveData<SignUpResponse>()

    val repository = UserRepository(ApiFactory.brandattyAPI)

    init {

    }

    fun doSignUp() {
        scope.launch {
            loading.postValue(true)
            val response = if (validate()) repository.doSigUp(
                name.value.toString(),
                email.value.toString(),
                phone.value.toString(),
                password.value.toString()
            ) else null

            loading.postValue(false)

            response?.let {
                loginResponse.postValue(it)
            }
        }
    }

    fun validate(): Boolean {
        when {
            name.value.isNullOrEmpty() -> phoneError.postValue(R.string.empty_name)
            email.value.isNullOrEmpty() -> phoneError.postValue(R.string.empty_email)
            phone.value.isNullOrEmpty() -> phoneError.postValue(R.string.empty_phone)
            password.value.isNullOrEmpty() -> passwordError.postValue(R.string.empty_password)
            else -> return true
        }
        return false
    }
}