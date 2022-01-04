package com.owlix.vendor.data.rest

import com.owlix.vendor.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
@JvmSuppressWildcards
interface ProductDao {

    @Multipart
    @POST("partner/create_partner_item")
    fun createProduct(
        @Part image: List<MultipartBody.Part>,
        @PartMap text:Map<String, RequestBody>
    ): Call<BaseProductResp>

    @Multipart
    @POST("partner/update_partner_item")
    fun updateProduct(
        @Part image: List<MultipartBody.Part>,
        @PartMap text:Map<String, RequestBody>
    ): Call<BaseProductResp>

    @POST("partner/update_partner_item")
    @FormUrlEncoded
    fun updateStock(
        @Field("_method") method:String,
        @Field("id") productId:String,
        @Field("in_stock") inStock:Boolean
    ): Call<BaseProductResp>

    @GET("partner/read_partner_item")
    fun getAllProduct(
//        @Query("offset") offset:Int,
//        @Query("limit") limit:Int,
//        @Query("latitude") latitude:Double,
//        @Query("longitude") longitude:Double
    ): Call<ProductResp>

    @GET("partner/read_partner_item")
    fun getProductDetail(
        @Query("id_partner_item") id_product:Int,
    ): Call<ProductDetailResp>

    @GET("partner/read_item_categories")
    fun getAllCategory(
    ): Call<CategoryResp>

    @DELETE("partner/delete_partner_item")
    fun deleteProduct(
        @Query("id") productId:Int
    ): Call<BaseProductResp>

    @DELETE("partner/delete_partner_item_image")
    fun deleteProductImage(
        @Query("id") productId:Int
    ): Call<BaseProductResp>
}