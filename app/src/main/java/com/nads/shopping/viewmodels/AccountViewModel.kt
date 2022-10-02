package com.nads.shopping.viewmodels

import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import com.nads.shopping.api.ApiFactory
import com.nads.shopping.datamodels.BaseModel
import com.nads.shopping.datamodels.Profile
import com.nads.shopping.repositories.ProfileRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File

class AccountViewModel : BaseViewModel() {

    val loading = MutableLiveData<Boolean>()
    private val profile = LiveEvent<BaseModel<Profile>>()
    val profileLoadEvent = profile
    val selectedImage = MutableLiveData<File>()

    private val profileUpdate = LiveEvent<BaseModel<Profile>>()
    val profileUpdateEvent = profileUpdate

    private val repository = ProfileRepository(ApiFactory.brandattyAPI)

    init {
    }

    fun getBasicInfo(token: String) {
        mainScope.launch {
            loading.postValue(true)
            val response = repository.getBasicInfo(token)

            loading.postValue(false)

            response?.let {
                profile.value = it
            }
        }
    }

    fun updateBasicInfo(token: String, gender:String) {
        mainScope.launch {
            loading.postValue(true)
            var multiPartImage: MultipartBody.Part? = if (selectedImage.value != null)
                ApiFactory.getRequest("photo", selectedImage.value) else null
            val response = repository.updateBasicInfo(
                token,
                profileLoadEvent.value?.data?.name.toString(),
                profileLoadEvent.value?.data?.email.toString(),
                profileLoadEvent.value?.data?.phone.toString(),
                profileLoadEvent.value?.data?.dob.toString(),
                gender,
                multiPartImage
            )

            loading.postValue(false)

            response?.let {
                profile.value = it
                profileUpdate.value = it
            }
        }
    }

}
