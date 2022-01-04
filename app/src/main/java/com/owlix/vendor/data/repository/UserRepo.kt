package com.owlix.vendor.data.repository

import com.mevou.customer.data.utils.RetrofitPrivateService
import com.mevou.customer.data.utils.RetrofitService
import com.owlix.vendor.constant.Message
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.*
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UserRepo {

    fun updateProfile(
        email: String,
        phoneNumber: String,
        provinceId: Int,
        cityId: Int,
        address: String,
        imageFile: File?,
        onResult: (isSuccess: Boolean, defaultNoDataResp: DefaultNoDataResp) -> Unit
    ){

        var body:MultipartBody.Part? = null

        imageFile?.let {
            val requestFile = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image_url", imageFile?.name, requestFile)
        }

        val map = HashMap<String, RequestBody>()
        map["_method"] = createPartFromString("PATCH")
        map["email"] = createPartFromString(email)
        map["phone_number"] = createPartFromString(phoneNumber)
        map["province_id"] = createPartFromString(provinceId.toString())
        map["city_id"] = createPartFromString(cityId.toString())
        map["address"] = createPartFromString(address)

        val call = RetrofitPrivateService.userDao.updateProfile(
            body,
            map
        )

        call.enqueue(object : Callback<DefaultNoDataResp> {

            override fun onResponse(call: Call<DefaultNoDataResp>, resp: Response<DefaultNoDataResp>) {
                if (resp.isSuccessful) {
                    resp.body()?.let {

                        if (it.status == Status.SUCCESS) {
                            onResult(true, it)
                        } else {
                            onResult(false, it)
                        }

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

    fun updateFCMToken(
        fcmToken:String,
        onResult: (isSuccess: Boolean, response: DefaultNoDataResp) -> Unit
    ) {

        val call =
            RetrofitPrivateService.userDao.updateFCMToken(fcmToken)

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

    fun getAllBanks(
        onResult: (isSuccess: Boolean, bankResp: BankResp) -> Unit
    ) {

        val call =
            RetrofitPrivateService.userDao.getAllBank()

        call.enqueue(object : Callback<BankResp> {
            override fun onResponse(
                call: Call<BankResp>,
                response: Response<BankResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = BankResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<BankResp>, t: Throwable) {
                val response = BankResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }
        })
    }

    fun createWithdraw(
        bank:Bank,
        amount: Int,
        onResult: (isSuccess: Boolean, response: WithdrawResp) -> Unit
    ){
        val bankCode = bank.code
        val accountName = bank.accountName
        val accountNumber = bank.accountNumber

        val call = RetrofitPrivateService.userDao.createWithdraw(bankCode, accountName, accountNumber, amount)
        call.enqueue(object : Callback<WithdrawResp> {

            override fun onResponse(call: Call<WithdrawResp>, resp: Response<WithdrawResp>) {
                if (resp.isSuccessful) {
                    resp.body()?.let {

                        if (it.status == Status.SUCCESS) {
                            onResult(true, it)
                        } else {
                            onResult(false, it)
                        }

                    } ?: run {
                        val response = WithdrawResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }
            }

            override fun onFailure(call: Call<WithdrawResp>, t: Throwable) {
                val response = WithdrawResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }

        })

    }

    fun getAllWithdraw(
        status:String,
        onResult: (isSuccess: Boolean, response: WithdrawHistoryResp) -> Unit
    ){

        val call = RetrofitPrivateService.userDao.getAllWithdraw(status)
        call.enqueue(object : Callback<WithdrawHistoryResp> {

            override fun onResponse(call: Call<WithdrawHistoryResp>, resp: Response<WithdrawHistoryResp>) {
                if (resp.isSuccessful) {
                    resp.body()?.let {

                        if (it.status == Status.SUCCESS) {
                            onResult(true, it)
                        } else {
                            onResult(false, it)
                        }

                    } ?: run {
                        val response = WithdrawHistoryResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }
            }

            override fun onFailure(call: Call<WithdrawHistoryResp>, t: Throwable) {
                val response = WithdrawHistoryResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }

        })

    }

    fun updateSubscription(
        expireDate: String,
        onResult: (isSuccess: Boolean, defaultResp: DefaultResp) -> Unit
    ){

        val call =
            RetrofitPrivateService.userDao.updateSubscription(expireDate)

        call.enqueue(object : Callback<DefaultResp> {
            override fun onResponse(
                call: Call<DefaultResp>,
                response: Response<DefaultResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(true, it)

                    } ?: run {
                        val response = DefaultResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }

            }

            override fun onFailure(call: Call<DefaultResp>, t: Throwable) {
                val response = DefaultResp()
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
        private var INSTANCE: UserRepo? = null
        fun getInstance() = INSTANCE
            ?: UserRepo().also {
                INSTANCE = it
            }
    }
}