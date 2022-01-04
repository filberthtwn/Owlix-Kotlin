package com.mevou.customer.data.utils

import com.owlix.vendor.data.repository.OrderRepo
import com.owlix.vendor.data.rest.*
import com.owlix.vendor.helper.UserPreferenceHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitPrivateService {
    var token =  UserPreferenceHelper.getToken()

    companion object {
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor(ApiLogger())
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + RetrofitPrivateService().token)
                        .build()
                    return chain.proceed(request)
                }
            })
            .addInterceptor(interceptor).build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val userDao: UserDao = retrofit.create(UserDao::class.java)
        val authDao: AuthDao = retrofit.create(AuthDao::class.java)
        val orderDao: OrderDao = retrofit.create(OrderDao::class.java)
        val productDao: ProductDao = retrofit.create(ProductDao::class.java)
        val notifDao: NotifDao = retrofit.create(NotifDao::class.java)

    }
}