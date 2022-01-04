package com.owlix.vendor.data.model

import com.google.gson.annotations.SerializedName

class DefaultResp {
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data:String = ""
}

class DefaultNoDataResp {
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""
}