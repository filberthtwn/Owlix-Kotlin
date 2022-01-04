package com.owlix.vendor.data.model

import com.google.gson.annotations.SerializedName

class OrderResp{
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var orders:List<Order>? = null

}

class Order {

    @SerializedName("id")
    var id: Int = -1

    @SerializedName("external_id")
    var invoiceId: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("status")
    var status: String = ""

    @SerializedName("images")
    var imageObjects: List<ImageObject> = arrayListOf()
//
//    @SerializedName("partner_item_price")
//    var price: Int = 0
//
//    @SerializedName("status")
//    var quantity: Int = 0

//
    @SerializedName("created_at")
    var createdAt: String = ""

    @SerializedName("customer")
    var buyer: User? = null
}

class OrderDetailResp{

    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data: OrderDetail? = null
}

class OrderDetail{

    @SerializedName("external_id")
    var invoiceId: String = ""

    @SerializedName("status")
    var status: String = ""

    @SerializedName("amount")
    var totalAmount: Int = 0

    @SerializedName("delivery_expense")
    var deliveryFee: Int = 0

    @SerializedName("created_at")
    var createdAt: String = ""

    @SerializedName("customer")
    var buyer: User? = null

    @SerializedName("order_items")
    var orderItem: List<OrderProductItem> = arrayListOf()
}

class OrderItem{


    @SerializedName("partner_item")
    var product: OrderProductItem = OrderProductItem()
}

class OrderProductItem{
    @SerializedName("id")
    var id: Int = -1

    @SerializedName("name")
    var name: String = ""

    @SerializedName("quantity")
    var quantity: Int = 0

    @SerializedName("description")
    var description:String = ""

    @SerializedName("weight")
    var weight:String = ""

    @SerializedName("image_url")
    var imageUrl:String = ""

    @SerializedName("store_item_price")
    var storePrice:Int = 0

    @SerializedName("partner_item_price")
    var price:Int = 0

    @SerializedName("id_item_category")
    var categoryId:Int = 0

    @SerializedName("partner_item_images")
    var images:List<ImageObject> = arrayListOf()
}


class ImageObject{
    @SerializedName("id")
    var id: Int = -1

    @SerializedName("image_url")
    var image_url: String = ""
}