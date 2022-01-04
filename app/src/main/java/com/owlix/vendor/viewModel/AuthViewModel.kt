package com.owlix.vendor.viewModel

import androidx.lifecycle.MutableLiveData
import com.owlix.vendor.data.model.*
import com.owlix.vendor.data.repository.AuthRepo
import java.io.File

class AuthViewModel {
    private val authRepo:AuthRepo = AuthRepo()
    var isLoginSuccess = MutableLiveData<Boolean>()
    var loginResp = MutableLiveData<LoginResp>()
    var userResp = MutableLiveData<UserResp>()

    var verifyResp = MutableLiveData<VerifyResp>()
    var provinceResp = MutableLiveData<ProvinceResp>()
    var cityResp = MutableLiveData<CityResp>()
    var partnerCategoryResp = MutableLiveData<PartnerCategoryResp>()
    var updateStoreInfoResp = MutableLiveData<DefaultNoDataResp>()


    fun login(phoneNumber:String){
        AuthRepo.getInstance().login(phoneNumber) { isSuccess, response ->
            this.isLoginSuccess.postValue(isSuccess)
            this.loginResp.postValue(response)
        }
    }

    fun signup(name:String, email:String, phoneNumber:String, provinceId:Int, cityId:Int, address:String, categoryId:Int, imageFile:File?){
        AuthRepo.getInstance().signup(name, email,phoneNumber,provinceId,cityId,address,categoryId,imageFile){ isSuccess, response ->
            this.verifyResp.postValue(response)
        }
    }

    fun logout(){
        AuthRepo.getInstance().logout(){ isSuccess, response ->
            this.loginResp.postValue(response)
        }
    }

    fun verifyOTP(phoneNumber:String, verifyCode:String){
        AuthRepo.getInstance().verifyOTP(phoneNumber, verifyCode){ isSuccess, response ->
            this.verifyResp.postValue(response)
        }
    }

    fun getUserDetail(){
        AuthRepo.getInstance().getUserDetail{ isSuccess, response ->
            this.userResp.postValue(response)
        }
    }

    fun getAllProvince(){
        AuthRepo.getInstance().getAllProvince{ isSuccess, response ->
            this.provinceResp.postValue(response)
        }
    }

    fun getAllCity(provinceId:Int){
        AuthRepo.getInstance().getAllCity(provinceId){ isSuccess, response ->
            this.cityResp.postValue(response)
        }
    }

    fun getAllCategory(){
        AuthRepo.getInstance().getAllCategory { isSuccess, partnerCategoryResp ->
            this.partnerCategoryResp.postValue(partnerCategoryResp)
        }
    }

    fun updatePersonalInfo(user:User){
        AuthRepo.getInstance().updateStoreInfo(user) { isSuccess, response ->
            this.updateStoreInfoResp.postValue(response)
        }
    }

    fun updateStoreInfo(user:User){
        AuthRepo.getInstance().updateStoreInfo(user) { isSuccess, response ->
            this.updateStoreInfoResp.postValue(response)
        }
    }
}