package com.owlix.vendor.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.mevou.customer.data.utils.RetrofitPrivateService
import com.owlix.vendor.constant.Message
import com.owlix.vendor.data.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class ProductRepo {

    fun createProduct(
        product: Product,
        images: List<File>,
        onResult: (isSuccess: Boolean, response: BaseProductResp) -> Unit
    ){

        var parts:ArrayList<MultipartBody.Part> = arrayListOf()

        for (idx in images.indices){
            val originalBitmap = BitmapFactory.decodeFile(images[idx].path)
            val baos = ByteArrayOutputStream()
            originalBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos)
            val byteArray = baos.toByteArray()

            var fos:FileOutputStream? = null
            try {
                fos = FileOutputStream(images[idx])
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            try {
                fos!!.write(byteArray)
                fos!!.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }


            val requestFile = images[idx].asRequestBody("multipart/form-data".toMediaTypeOrNull())
            parts.add(
                MultipartBody.Part.createFormData(
                    "partner_item_image[$idx]",
                    images[idx].name,
                    requestFile
                )
            )
        }

        val map = HashMap<String, RequestBody>()
        map["name"] = createPartFromString(product.name)
        map["description"] = createPartFromString(product.description)
        map["weight"] = createPartFromString(product.weight)
        map["partner_item_price"] = createPartFromString(product.price.toString())
        map["id_item_category"] = createPartFromString(product.categoryId.toString())

        val call =
            RetrofitPrivateService.productDao.createProduct(
                parts,
                map
            )

        call.enqueue(object : Callback<BaseProductResp> {
            override fun onResponse(
                call: Call<BaseProductResp>,
                response: Response<BaseProductResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = BaseProductResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<BaseProductResp>, t: Throwable) {
                val response = BaseProductResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    fun updateProduct(
        product: Product,
        images: List<File>,
        onResult: (isSuccess: Boolean, response: BaseProductResp) -> Unit
    ){
//        val call =
//            RetrofitPrivateService.productDao.updateProduct(
//                "PATCH",
//                product.id.toString(),
//                product.name,
//                product.description,
//                product.weight,
//                product.price.toString(),
//                product.categoryId
//            )

        var parts:ArrayList<MultipartBody.Part> = arrayListOf()

        for (idx in images.indices){
            val originalBitmap = BitmapFactory.decodeFile(images[idx].path)
            val baos = ByteArrayOutputStream()
            originalBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos)
            val byteArray = baos.toByteArray()

            var fos:FileOutputStream? = null
            try {
                fos = FileOutputStream(images[idx])
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            try {
                fos!!.write(byteArray)
                fos!!.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }


            val requestFile = images[idx].asRequestBody("multipart/form-data".toMediaTypeOrNull())
            parts.add(
                MultipartBody.Part.createFormData(
                    "partner_item_image[$idx]",
                    images[idx].name,
                    requestFile
                )
            )
        }

        val map = HashMap<String, RequestBody>()
        map["_method"] = createPartFromString("PATCH")
        map["id"] = createPartFromString(product.id.toString())
        map["name"] = createPartFromString(product.name)
        map["description"] = createPartFromString(product.description)
        map["weight"] = createPartFromString(product.weight)
        map["partner_item_price"] = createPartFromString(product.price.toString())
        map["id_item_category"] = createPartFromString(product.categoryId.toString())

        val call =
            RetrofitPrivateService.productDao.updateProduct(
                parts,
                map
            )

        call.enqueue(object : Callback<BaseProductResp> {
            override fun onResponse(
                call: Call<BaseProductResp>,
                response: Response<BaseProductResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = BaseProductResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<BaseProductResp>, t: Throwable) {
                val response = BaseProductResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    fun updateStock(
        productId: Int,
        status:Boolean,
        onResult: (isSuccess: Boolean, response: BaseProductResp) -> Unit
    ){
        val call =
            RetrofitPrivateService.productDao.updateStock(
                "PATCH",
                productId.toString(),
                status
            )

        call.enqueue(object : Callback<BaseProductResp> {
            override fun onResponse(
                call: Call<BaseProductResp>,
                response: Response<BaseProductResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = BaseProductResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<BaseProductResp>, t: Throwable) {
                val response = BaseProductResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    fun deleteProduct(
        productId: Int,
        onResult: (isSuccess: Boolean, response: BaseProductResp) -> Unit
    ){
        val call =
            RetrofitPrivateService.productDao.deleteProduct(productId)

        call.enqueue(object : Callback<BaseProductResp> {
            override fun onResponse(
                call: Call<BaseProductResp>,
                response: Response<BaseProductResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = BaseProductResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<BaseProductResp>, t: Throwable) {
                val response = BaseProductResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    fun deleteProductImage(
        imageId: Int,
        onResult: (isSuccess: Boolean, response: BaseProductResp) -> Unit
    ){
        val call =
            RetrofitPrivateService.productDao.deleteProductImage(imageId)

        call.enqueue(object : Callback<BaseProductResp> {
            override fun onResponse(
                call: Call<BaseProductResp>,
                response: Response<BaseProductResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = BaseProductResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<BaseProductResp>, t: Throwable) {
                val response = BaseProductResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    fun getAllProduct(
        onResult: (isSuccess: Boolean, productResp: ProductResp) -> Unit
    ) {

        val restoListCall =
            RetrofitPrivateService.productDao.getAllProduct()

        restoListCall.enqueue(object : Callback<ProductResp> {
            override fun onResponse(
                call: Call<ProductResp>,
                response: Response<ProductResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = ProductResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<ProductResp>, t: Throwable) {
                val response = ProductResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    fun getProductDetail(
        productId: Int,
        onResult: (isSuccess: Boolean, productDetailResp: ProductDetailResp) -> Unit
    ){
        val call =
            RetrofitPrivateService.productDao.getProductDetail(productId)

        call.enqueue(object : Callback<ProductDetailResp> {
            override fun onResponse(
                call: Call<ProductDetailResp>,
                response: Response<ProductDetailResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = ProductDetailResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<ProductDetailResp>, t: Throwable) {
                val response = ProductDetailResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    fun getAllCategory(
        onResult: (isSuccess: Boolean, response: CategoryResp) -> Unit
    ){
        val call =
            RetrofitPrivateService.productDao.getAllCategory()

        call.enqueue(object : Callback<CategoryResp> {
            override fun onResponse(
                call: Call<CategoryResp>,
                response: Response<CategoryResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = CategoryResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<CategoryResp>, t: Throwable) {
                val response = CategoryResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return descriptionString
            .toRequestBody(MultipartBody.FORM)
    }

    companion object {
        private var INSTANCE: ProductRepo? = null
        fun getInstance() = INSTANCE
            ?: ProductRepo().also {
                INSTANCE = it
            }
    }
}