package com.owlix.vendor.data.model

import com.google.gson.annotations.SerializedName

class CategoryResp {
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data:List<Category> = arrayListOf()
}

class Category {
    @SerializedName("id")
    var id: Int = -1

    @SerializedName("name")
    var name: String = ""
}