package com.nads.shopping.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.nads.shopping.BuildConfig
import com.nads.shopping.utils.imageData
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ApiFactory{

    //Creating Auth Interceptor to add api_key query in front of all the requests.
/*    private val authInterceptor = Interceptor {chain->
//        val newUrl = chain.request().url()
//            .newBuilder()
////            .addQueryParameter("api_key", AppConstants.tmdbApiKey)
//            .build()

        val newRequest = chain.request()
            .newBuilder()
//            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }*/

    //OkhttpClient for building http request url

    private fun getClient() : OkHttpClient{
        val retrofitLogging = HttpLoggingInterceptor()
        retrofitLogging.level = HttpLoggingInterceptor.Level.BODY

        val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        clientBuilder.addInterceptor(retrofitLogging)

        return clientBuilder.build()
    }

    private fun retrofit() : Retrofit = Retrofit.Builder()
        .client(getClient())
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    val brandattyAPI : EndPoints = retrofit().create(EndPoints::class.java)

    fun getRequest(paramName: String, file: File?
    ): MultipartBody.Part {
        return MultipartBody.Part.createFormData(paramName, file?.name, imageData(file!!))
    }
}