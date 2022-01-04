package com.owlix.vendor.data.rest

import com.owlix.vendor.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

@JvmSuppressWildcards
interface AuthDao {

    @POST("partner/login")
    @FormUrlEncoded
    fun login(
        @Field("phone_number") phoneNumber:String
    ): Call<LoginResp>

    @Multipart
    @POST("partner/register")
    fun signup(
        @Part image:MultipartBody.Part?,
        @PartMap text:Map<String,RequestBody>
    ): Call<VerifyResp>

    @GET("partner/logout")
    fun logout(): Call<LoginResp>

    @POST("partner/verify")
    @FormUrlEncoded
    fun otpVerify(
        @Field("phone_number") phoneNumber:String,
        @Field("verification_code") verifyCode:String
    ): Call<VerifyResp>

    @GET("partner/detail")
    fun getUserDetail(
    ): Call<UserResp>

    @GET("read_province")
    fun getAllProvince(
    ): Call<ProvinceResp>

    @GET("read_city")
    fun getAllCity(
        @Query("province_id") provinceId:Int,
    ): Call<CityResp>

    @GET("partner/read_partner_categories")
    fun getAllPartnerCategory(
    ): Call<PartnerCategoryResp>

    @PATCH("partner/update_category_and_description")
    fun updatePersonalInfo(
        @Query("_method") method:String,
        @Query("phone_number") phoneNumber:String,
        @Query("description") description:Int
    ): Call<DefaultNoDataResp>

    @PATCH("partner/update_category_and_description")
    fun updateStoreInfo(
        @Query("name") name:String,
        @Query("description") description:String,
        @Query("id_partner_category") categoryId:Int
    ): Call<DefaultNoDataResp>

}