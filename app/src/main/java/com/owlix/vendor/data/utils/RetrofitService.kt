package com.mevou.customer.data.utils

import com.owlix.vendor.data.rest.AuthDao
import com.owlix.vendor.data.rest.UserDao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://production.owlix-id.com/api/"

class RetrofitService {
    companion object {

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor(ApiLogger())
            .setLevel(HttpLoggingInterceptor.Level.BODY)
//
        var client = OkHttpClient.Builder()
            .addInterceptor(interceptor).build()
//
        private val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
//            .build()
        private val retrofit = retrofitBuilder.build()

//        val userDao: UserDao = RetrofitPrivateService.retrofit.create(UserDao::class.java)
        val authDao:AuthDao = retrofit.create(AuthDao::class.java)
    }
}