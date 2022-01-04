package com.owlix.vendor.viewModel

import androidx.lifecycle.MutableLiveData
import com.owlix.vendor.data.model.DefaultNoDataResp
import com.owlix.vendor.data.model.DefaultResp
import com.owlix.vendor.data.model.OrderDetailResp
import com.owlix.vendor.data.model.OrderResp
import com.owlix.vendor.data.repository.OrderRepo

class OrderViewModel {
    var orderResp = MutableLiveData<OrderResp>()
    var orderDetailResp = MutableLiveData<OrderDetailResp>()
    var rejectOrderResp = MutableLiveData<DefaultNoDataResp>()
    var updateReceiptCodeResp = MutableLiveData<DefaultNoDataResp>()

    fun getAllOrder(status: String) {
        OrderRepo.getInstance().getAllOrder(status) { isSuccess, response ->
            this.orderResp.postValue(response)
        }
    }

    fun getOrderDetail(orderId: Int) {
        OrderRepo.getInstance().getOrderDetail(orderId) { isSuccess, response ->
            this.orderDetailResp.postValue(response)
        }
    }

    fun rejectOrder(orderId: Int){
        OrderRepo.getInstance().rejectOrder(orderId){ isSuccess, response ->
            this.rejectOrderResp.postValue(response)
        }
    }

    fun updateReceiptCode(orderId: Int, receiptCode:String){
        OrderRepo.getInstance().updateReceiptCode(orderId, receiptCode){ isSuccess, response ->
            this.updateReceiptCodeResp.postValue(response)
        }
    }
}