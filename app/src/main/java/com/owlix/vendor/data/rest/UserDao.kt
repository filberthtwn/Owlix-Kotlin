package com.owlix.vendor.data.rest

import com.owlix.vendor.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

@JvmSuppressWildcards
interface UserDao {
    @GET("partner/read_disbursement_available_banks")
    fun getAllBank(

    ): Call<BankResp>

    @POST("partner/create_withdraw")
    @FormUrlEncoded
    fun createWithdraw(
        @Field("bank_code") bankCode:String,
        @Field("account_holder_name") accountName:String,
        @Field("account_number") accountNumber:String,
        @Field("amount") amount:Int
    ): Call<WithdrawResp>

    @GET("partner/read_by_status")
    fun getAllWithdraw(
        @Query("status") status:String
    ): Call<WithdrawHistoryResp>

    @Multipart
    @POST("partner/update")
    fun updateProfile(
        @Part image: MultipartBody.Part?,
        @PartMap text:Map<String, RequestBody>
    ): Call<DefaultNoDataResp>

    @PATCH("partner/update_fcm_token")
    fun updateFCMToken(
        @Query("fcm_token") fcmToken:String
    ): Call<DefaultNoDataResp>

    @PATCH("partner/update_subscription")
    fun updateSubscription(
        @Query("expired_date") expireDate:String
    ): Call<DefaultResp>
}