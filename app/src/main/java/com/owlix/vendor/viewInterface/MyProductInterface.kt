package com.owlix.vendor.viewInterface

import com.owlix.vendor.data.model.Product

interface MyProductInterface {
    fun onProductSelected(product: Product){}
    fun onImageRemoved(idx:Int, isLocal:Boolean){}
    fun stockUpdate(productId:Int, status:Boolean){}
}