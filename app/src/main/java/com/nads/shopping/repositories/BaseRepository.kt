package com.nads.shopping.repositories

import android.util.Log
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import com.nads.shopping.api.Result as APIResult

open class BaseRepository {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {

        val result : APIResult<T> = safeApiResult(call,errorMessage)
        var data : T? = null

        when(result) {
            is APIResult.Success ->
                data = result.data
            is APIResult.Error -> {
                Log.d("1.DataRepository", "$errorMessage & Exception - ${result.exception}")
            }
        }


        return data

    }

    private suspend fun <T: Any> safeApiResult(call: suspend ()-> Response<T>, errorMessage: String) : APIResult<T>{
       try {
           val response = call.invoke()
           if(response.isSuccessful) return APIResult.Success(response.body()!!)
       } catch (e:Exception){
           return APIResult.Error(e)
       }
        return APIResult.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))

    }
}