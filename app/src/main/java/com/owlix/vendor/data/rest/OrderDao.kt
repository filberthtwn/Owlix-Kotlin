package com.owlix.vendor.data.rest

import com.owlix.vendor.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface OrderDao {

    @GET("partner/read_orders")
    fun getAllOrder(
        @Query("status") status:String
    ): Call<OrderResp>

    @GET("partner/read_orders")
    fun getOrderDetail(
        @Query("id_order") orderId:Int
    ): Call<OrderDetailResp>

    @PATCH("partner/update")
    fun updateReceiptCode(
        @Query("id_order") orderId:Int,
        @Query("receipt_number") receiptCode:String
    ): Call<DefaultNoDataResp>

    @PATCH("partner/reject_order")
    fun rejectOrder(
        @Query("id") orderId:Int,
    ): Call<DefaultNoDataResp>
}