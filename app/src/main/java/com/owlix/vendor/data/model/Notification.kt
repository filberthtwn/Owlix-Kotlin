package com.owlix.vendor.data.model

import com.google.gson.annotations.SerializedName

class NotifResp{
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var notifications:List<Notification> = arrayListOf()
}

class Notification{
    @SerializedName("title")
    var title: String = ""

    @SerializedName("message")
    var message: String = ""
}