package com.owlix.vendor.data.repository

import com.mevou.customer.data.utils.RetrofitPrivateService
import com.owlix.vendor.constant.Message
import com.owlix.vendor.data.model.*
import com.owlix.vendor.helper.UserPreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class OrderRepo {
    fun getAllOrder(
        status:String,
        onResult: (isSuccess: Boolean, orderResp: OrderResp) -> Unit
    ) {

        val call =
            RetrofitPrivateService.orderDao.getAllOrder(status)

        call.enqueue(object : Callback<OrderResp> {
            override fun onResponse(
                call: Call<OrderResp>,
                response: Response<OrderResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = OrderResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<OrderResp>, t: Throwable) {
                val response = OrderResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    fun getOrderDetail(
        orderId:Int,
        onResult: (isSuccess: Boolean, response: OrderDetailResp) -> Unit
    ) {

        val call =
            RetrofitPrivateService.orderDao.getOrderDetail(orderId)

        call.enqueue(object : Callback<OrderDetailResp> {
            override fun onResponse(
                call: Call<OrderDetailResp>,
                response: Response<OrderDetailResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = OrderDetailResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<OrderDetailResp>, t: Throwable) {
                val response = OrderDetailResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    fun updateReceiptCode(
        orderId:Int,
        receiptCode:String,
        onResult: (isSuccess: Boolean, response: DefaultNoDataResp) -> Unit
    ) {

        val call =
            RetrofitPrivateService.orderDao.updateReceiptCode(orderId, receiptCode)

        call.enqueue(object : Callback<DefaultNoDataResp> {
            override fun onResponse(
                call: Call<DefaultNoDataResp>,
                response: Response<DefaultNoDataResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = DefaultNoDataResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<DefaultNoDataResp>, t: Throwable) {
                val response = DefaultNoDataResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    fun rejectOrder(
        orderId:Int,
        onResult: (isSuccess: Boolean, response: DefaultNoDataResp) -> Unit
    ) {

        val call =
            RetrofitPrivateService.orderDao.rejectOrder(orderId)

        call.enqueue(object : Callback<DefaultNoDataResp> {
            override fun onResponse(
                call: Call<DefaultNoDataResp>,
                response: Response<DefaultNoDataResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = DefaultNoDataResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<DefaultNoDataResp>, t: Throwable) {
                val response = DefaultNoDataResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    companion object {
        private var INSTANCE: OrderRepo? = null
        fun getInstance() = INSTANCE
            ?: OrderRepo().also {
                INSTANCE = it
            }
    }
}