package com.owlix.vendor.data.model

import com.google.gson.annotations.SerializedName

class BaseProductResp{
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

//    @SerializedName("data")
//    var products:List<Product>? = null
}

class UpdateProductResp{
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

//    @SerializedName("data")
//    var products:List<Product>? = null
}

class ProductResp {
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var products:List<Product>? = null
}

class ProductDetailResp {
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var product:Product? = null
}

class Product {
    @SerializedName("id")
    var id: Int = -1

    @SerializedName("name")
    var name: String = ""

    @SerializedName("description")
    var description:String = ""

    @SerializedName("weight")
    var weight:String = ""

    @SerializedName("in_stock")
    var inStock:Boolean = false

    @SerializedName("image_url")
    var imageUrl:String = ""

    @SerializedName("partner_item_price")
    var price:Int = 0

    @SerializedName("id_item_category")
    var categoryId:Int = 0

    @SerializedName("images")
    var images:List<ImageObject> = arrayListOf()
}