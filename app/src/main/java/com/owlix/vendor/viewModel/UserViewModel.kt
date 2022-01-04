package com.owlix.vendor.viewModel

import androidx.lifecycle.MutableLiveData
import com.owlix.vendor.data.model.*
import com.owlix.vendor.data.repository.AuthRepo
import com.owlix.vendor.data.repository.ProductRepo
import com.owlix.vendor.data.repository.UserRepo
import java.io.File

class UserViewModel {
    private val userRepo: UserRepo = UserRepo()

    var bankResp = MutableLiveData<BankResp>()
    var withdrawResp = MutableLiveData<WithdrawResp>()
    var withdrawHistoryResp = MutableLiveData<WithdrawHistoryResp>()
    var updatePersonalInfoResp = MutableLiveData<DefaultNoDataResp>()
    var updateFCMTokenResp = MutableLiveData<DefaultNoDataResp>()
    var updateSubscriptionResp = MutableLiveData<DefaultResp>()

    fun updateProfile(email:String, phoneNumber:String, provinceId:Int, cityId:Int, address:String, imageFile: File?){
        UserRepo.getInstance().updateProfile(email, phoneNumber,provinceId, cityId, address,imageFile){ isSuccess, response ->
            this.updatePersonalInfoResp.postValue(response)
        }
    }

    fun updateFCMToken(fcmToken:String){
        UserRepo.getInstance().updateFCMToken(fcmToken){ isSuccess, response ->
            this.updateFCMTokenResp.postValue(response)
        }
    }

    fun getAllBank(){
        UserRepo.getInstance().getAllBanks { isSuccess, response ->
            this.bankResp.postValue(response)
        }


    }

    fun createWithdraw(bank:Bank, amount:Int){
        UserRepo.getInstance().createWithdraw(bank, amount){ isSuccess, response ->
            this.withdrawResp.postValue(response)
        }
    }

    fun getAllWithdraw(status:String){
        UserRepo.getInstance().getAllWithdraw(status){isSuccess, response ->
            this.withdrawHistoryResp.postValue(response)
        }
    }

    fun updateSubscription(expireDate: String){
        UserRepo.getInstance().updateSubscription(expireDate){isSuccess, response ->
            this.updateSubscriptionResp.postValue(response)
        }
    }
}