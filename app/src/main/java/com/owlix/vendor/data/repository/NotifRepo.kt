package com.owlix.vendor.data.repository

import com.mevou.customer.data.utils.RetrofitPrivateService
import com.owlix.vendor.constant.Message
import com.owlix.vendor.data.model.NotifResp
import com.owlix.vendor.data.model.OrderResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotifRepo {
    fun getAllNotification(onResult: (isSuccess: Boolean, response: NotifResp) -> Unit
    ) {

        val call =
            RetrofitPrivateService.notifDao.getAllNotification()

        call.enqueue(object : Callback<NotifResp> {
            override fun onResponse(
                call: Call<NotifResp>,
                response: Response<NotifResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = NotifResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                } else {
                    val response = NotifResp()
                    response.message = Message.Error.BODY_MISSING

                    onResult(false, response)
                }
            }

            override fun onFailure(call: Call<NotifResp>, t: Throwable) {
                val response = NotifResp()
                response.message = t.localizedMessage!!

                onResult(false, response)
            }
        })
    }

    companion object {
        private var INSTANCE: NotifRepo? = null
        fun getInstance() = INSTANCE
            ?: NotifRepo().also {
                INSTANCE = it
            }
    }
}