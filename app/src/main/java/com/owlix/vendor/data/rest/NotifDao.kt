package com.owlix.vendor.data.rest

import com.owlix.vendor.data.model.NotifResp
import retrofit2.Call
import retrofit2.http.GET

interface NotifDao {
    @GET("partner/read_notification")
    fun getAllNotification(

    ): Call<NotifResp>
}