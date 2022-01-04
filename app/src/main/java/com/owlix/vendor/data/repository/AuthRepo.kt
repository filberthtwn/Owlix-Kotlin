package com.owlix.vendor.data.repository

import com.mevou.customer.data.utils.RetrofitPrivateService
import com.mevou.customer.data.utils.RetrofitService
import com.owlix.vendor.constant.Message
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class AuthRepo {
//    private var status:MutableLiveData<String> = MutableLiveData()
//    private var successMsg: MutableLiveData<String> = MutableLiveData()
//    private var loginData:MutableLiveData<LoginRespData> = MutableLiveData()

    fun login(
        phoneNumber: String,
        onResult: (isSuccess: Boolean, loginResp: LoginResp) -> Unit
    ){

        val loginCall = RetrofitService.authDao.login(phoneNumber)

        loginCall.enqueue(object : Callback<LoginResp> {

            override fun onResponse(call: Call<LoginResp>, resp: Response<LoginResp>) {
                if (resp.isSuccessful) {
                    resp.body()?.let {

                        if (it.status == Status.SUCCESS) {
                            onResult(true, it)
                        } else {
                            onResult(false, it)
                        }

                    } ?: run {
                        val loginResp = LoginResp()
                        loginResp.message = Message.Error.BODY_MISSING

                        onResult(false, loginResp)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResp>, t: Throwable) {
                val loginResp = LoginResp()
                loginResp.message = t.localizedMessage

                onResult(false, loginResp)
            }

        })

    }

    fun signup(
        name: String,
        email: String,
        phoneNumber: String,
        provinceId: Int,
        cityId: Int,
        address: String,
        categoryId: Int,
        imageFile: File?,
        onResult: (isSuccess: Boolean, verifyResp: VerifyResp) -> Unit
    ){

        var body:MultipartBody.Part? = null

        imageFile?.let {
            val requestFile = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image_url", imageFile?.name, requestFile)
        }

        val map = HashMap<String, RequestBody>()
        map["name"] = createPartFromString(name)
        map["email"] = createPartFromString(email)
        map["phone_number"] = createPartFromString(phoneNumber)
        map["province_id"] = createPartFromString(provinceId.toString())
        map["city_id"] = createPartFromString(cityId.toString())
        map["address"] = createPartFromString(address)
        map["id_partner_category"] = createPartFromString(categoryId.toString())

        val call = RetrofitService.authDao.signup(
            body,
            map
        )

        call.enqueue(object : Callback<VerifyResp> {

            override fun onResponse(call: Call<VerifyResp>, resp: Response<VerifyResp>) {
                if (resp.isSuccessful) {
                    resp.body()?.let {

                        if (it.status == Status.SUCCESS) {
                            onResult(true, it)
                        } else {
                            onResult(false, it)
                        }

                    } ?: run {
                        val response = VerifyResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }
            }

            override fun onFailure(call: Call<VerifyResp>, t: Throwable) {
                val response = VerifyResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }

        })

    }

    fun logout(
        onResult: (isSuccess: Boolean, loginResp: LoginResp) -> Unit
    ){

        val call = RetrofitPrivateService.authDao.logout()

        call.enqueue(object : Callback<LoginResp> {

            override fun onResponse(call: Call<LoginResp>, resp: Response<LoginResp>) {
                if (resp.isSuccessful) {
                    resp.body()?.let {

                        if (it.status == Status.SUCCESS) {
                            onResult(true, it)
                        } else {
                            onResult(false, it)
                        }

                    } ?: run {
                        val loginResp = LoginResp()
                        loginResp.message = Message.Error.BODY_MISSING

                        onResult(false, loginResp)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResp>, t: Throwable) {
                val loginResp = LoginResp()
                loginResp.message = t.localizedMessage

                onResult(false, loginResp)
            }

        })

    }

    fun verifyOTP(
        phoneNumber: String,
        verifyCode: String,
        onResult: (isSuccess: Boolean, verifyResp: VerifyResp) -> Unit
    ){
        val loginCall = RetrofitService.authDao.otpVerify(phoneNumber, verifyCode)

        loginCall.enqueue(object : Callback<VerifyResp> {

            override fun onResponse(call: Call<VerifyResp>, resp: Response<VerifyResp>) {
                if (resp.isSuccessful) {
                    resp.body()?.let {

                        if (it.status == Status.SUCCESS) {
                            onResult(true, it)
                        } else {
                            onResult(false, it)
                        }

                    } ?: run {
                        val verifyResp = VerifyResp()
                        verifyResp.message = Message.Error.BODY_MISSING

                        onResult(false, verifyResp)
                    }
                }
            }

            override fun onFailure(call: Call<VerifyResp>, t: Throwable) {
                val verifyResp = VerifyResp()
                verifyResp.message = t.localizedMessage

                onResult(false, verifyResp)
            }

        })
    }

    fun getUserDetail(
        onResult: (isSuccess: Boolean, userResp: UserResp) -> Unit
    ){
        val call = RetrofitPrivateService.authDao.getUserDetail()

        call.enqueue(object : Callback<UserResp> {

            override fun onResponse(call: Call<UserResp>, resp: Response<UserResp>) {
                if (resp.isSuccessful) {
                    resp.body()?.let {

                        if (it.status == Status.SUCCESS) {
                            onResult(true, it)
                        } else {
                            onResult(false, it)
                        }

                    } ?: run {
                        val response = UserResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }
            }

            override fun onFailure(call: Call<UserResp>, t: Throwable) {
                val response = UserResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }

        })
    }

    fun getAllProvince(
        onResult: (isSuccess: Boolean, provinceResp: ProvinceResp) -> Unit
    ){
        val call = RetrofitService.authDao.getAllProvince()

        call.enqueue(object : Callback<ProvinceResp> {

            override fun onResponse(call: Call<ProvinceResp>, resp: Response<ProvinceResp>) {
                if (resp.isSuccessful) {
                    resp.body()?.let {

                        if (it.status == Status.SUCCESS) {
                            onResult(true, it)
                        } else {
                            onResult(false, it)
                        }

                    } ?: run {
                        val provinceResp = ProvinceResp()
                        provinceResp.message = Message.Error.BODY_MISSING

                        onResult(false, provinceResp)
                    }
                }
            }

            override fun onFailure(call: Call<ProvinceResp>, t: Throwable) {
                val provinceResp = ProvinceResp()
                provinceResp.message = t.localizedMessage

                onResult(false, provinceResp)
            }

        })
    }

    fun getAllCity(
        provinceId: Int,
        onResult: (isSuccess: Boolean, cityResp: CityResp) -> Unit
    ){
        val call = RetrofitService.authDao.getAllCity(provinceId)

        call.enqueue(object : Callback<CityResp> {

            override fun onResponse(call: Call<CityResp>, resp: Response<CityResp>) {
                if (resp.isSuccessful) {
                    resp.body()?.let {

                        if (it.status == Status.SUCCESS) {
                            onResult(true, it)
                        } else {
                            onResult(false, it)
                        }

                    } ?: run {
                        val cityResp = CityResp()
                        cityResp.message = Message.Error.BODY_MISSING

                        onResult(false, cityResp)
                    }
                }
            }

            override fun onFailure(call: Call<CityResp>, t: Throwable) {
                val cityResp = CityResp()
                cityResp.message = t.localizedMessage

                onResult(false, cityResp)
            }

        })
    }

    fun getAllCategory(
        onResult: (isSuccess: Boolean, partnerCategoryResp: PartnerCategoryResp) -> Unit
    ){
        val call = RetrofitService.authDao.getAllPartnerCategory()

        call.enqueue(object : Callback<PartnerCategoryResp> {

            override fun onResponse(
                call: Call<PartnerCategoryResp>,
                resp: Response<PartnerCategoryResp>
            ) {
                if (resp.isSuccessful) {
                    resp.body()?.let {

                        if (it.status == Status.SUCCESS) {
                            onResult(true, it)
                        } else {
                            onResult(false, it)
                        }

                    } ?: run {
                        val response = PartnerCategoryResp()
                        response.message = Message.Error.BODY_MISSING

                        onResult(false, response)
                    }
                }
            }

            override fun onFailure(call: Call<PartnerCategoryResp>, t: Throwable) {
                val response = PartnerCategoryResp()
                response.message = t.localizedMessage

                onResult(false, response)
            }

        })
    }

    fun updateStoreInfo(
        user:User,
        onResult: (isSuccess: Boolean, response: DefaultNoDataResp) -> Unit
    ){

        val name = user.name
        val description = user.description
        val categoryId = user.categoryId
        val call = RetrofitPrivateService.authDao.updateStoreInfo(name, description, categoryId)

        call.enqueue(object : Callback<DefaultNoDataResp> {

            override fun onResponse(
                call: Call<DefaultNoDataResp>,
                resp: Response<DefaultNoDataResp>
            ) {
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

    fun updatePersonalInfo(
        user:User,
        onResult: (isSuccess: Boolean, response: DefaultNoDataResp) -> Unit
    ){

        val method = "PATCH"
        val name = user.name
        val description = "Test"
        val categoryId = user.categoryId
        val call = RetrofitPrivateService.authDao.updatePersonalInfo(name, description, categoryId)

        call.enqueue(object : Callback<DefaultNoDataResp> {

            override fun onResponse(
                call: Call<DefaultNoDataResp>,
                resp: Response<DefaultNoDataResp>
            ) {
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

    private fun createPartFromString(descriptionString: String): RequestBody {
        return descriptionString
            .toRequestBody(MultipartBody.FORM)
    }

    companion object {
        private var INSTANCE: AuthRepo? = null
        fun getInstance() = INSTANCE
            ?: AuthRepo().also {
                INSTANCE = it
            }
    }

}