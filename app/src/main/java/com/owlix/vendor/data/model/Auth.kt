package com.owlix.vendor.data.model

import com.google.gson.annotations.SerializedName

class UserResp{
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data: User? = null
}

class User{
    @SerializedName("id")
    var id: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("email")
    var email: String = ""

    @SerializedName("image_url")
    var imageUrl: String = ""

    @SerializedName("phone_number")
    var phoneNumber: String = ""

    @SerializedName("address")
    var address: String = ""

    @SerializedName("balance")
    var balance: Int = 0

    @SerializedName("province_id")
    var provinceId: Int = 0

    @SerializedName("city_id")
    var cityId: Int = 0

    @SerializedName("id_partner_category")
    var categoryId: Int = -1

    @SerializedName("on_suspend")
    var onSuspend: String = "NO"

    @SerializedName("expired_date")
    var expireDate: String = ""

    @SerializedName("postal_code")
    var postalCode: String = ""
}

class LoginResp{
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data: String = ""
}

class VerifyResp{
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data: String = ""
}

class ProvinceResp{
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data:List<ProvinceData> = arrayListOf()
}

class ProvinceData{
    @SerializedName("province_id")
    var id: Int = 0

    @SerializedName("province")
    var name: String = ""
}

class CityResp{
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data:List<CityData> = arrayListOf()
}

class CityData{
    @SerializedName("city_id")
    var id: Int = 0

    @SerializedName("city_name")
    var name: String = ""

    @SerializedName("type")
    var type: String = ""
}

class PartnerCategoryResp{
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data:List<PartnerCategoryData> = arrayListOf()
}

class PartnerCategoryData{
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""
}