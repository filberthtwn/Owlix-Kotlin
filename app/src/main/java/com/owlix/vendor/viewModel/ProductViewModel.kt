package com.owlix.vendor.viewModel

import androidx.lifecycle.MutableLiveData
import com.owlix.vendor.data.model.*
import com.owlix.vendor.data.repository.ProductRepo
import java.io.File

class ProductViewModel {
    private val productRepo:ProductRepo = ProductRepo()
    var baseProductResp = MutableLiveData<BaseProductResp>()
    var deleteProductImageResp = MutableLiveData<BaseProductResp>()
    var productResp = MutableLiveData<ProductResp>()
    var categoryResp = MutableLiveData<CategoryResp>()
    var productDetailResp = MutableLiveData<ProductDetailResp>()

    fun createProduct(product:Product, images:List<File>){
        ProductRepo.getInstance().createProduct(product, images) { isSuccess, response ->
            this.baseProductResp.postValue(response)
        }
    }

    fun updateProduct(product:Product, images:List<File>){
        ProductRepo.getInstance().updateProduct(product, images) { isSuccess, response ->
            this.baseProductResp.postValue(response)
        }
    }

    fun updateStock(productId: Int, status:Boolean){
        ProductRepo.getInstance().updateStock(productId, status) { isSuccess, response ->
            this.baseProductResp.postValue(response)
        }
    }

    fun deleteProduct(productId:Int){
        ProductRepo.getInstance().deleteProduct(productId) { isSuccess, response ->
            this.baseProductResp.postValue(response)
        }
    }

    fun deleteProductImage(imageId:Int){
        ProductRepo.getInstance().deleteProductImage(imageId) { isSuccess, response ->
            this.deleteProductImageResp.postValue(response)
        }
    }

    fun getAllProduct(){
        ProductRepo.getInstance().getAllProduct { isSuccess, response ->
            this.productResp.postValue(response)
        }
    }

    fun getProductDetail(productId:Int) {
        ProductRepo.getInstance().getProductDetail(productId) { isSuccess, response ->
            this.productDetailResp.postValue(response)
        }
    }

    fun getAllCategory(){
        ProductRepo.getInstance().getAllCategory { isSuccess, response ->
            this.categoryResp.postValue(response)
        }
    }
}