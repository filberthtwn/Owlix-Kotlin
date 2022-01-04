package com.owlix.vendor.helper

import android.app.Activity
import android.content.Context
import com.owlix.vendor.data.model.Bank
import com.owlix.vendor.data.model.User

class UserPreferenceHelper {

    companion object{
        var activity:Activity? = null

        fun setShowOnBoarding(isShowed:Boolean){
            this.activity?.let {
                val editor = it.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                editor.putBoolean("isShowed", isShowed)
                editor.apply()
            }
        }

        fun getShowOnBoarding():Boolean{
            this.activity?.let {
                return it.getSharedPreferences("user", Context.MODE_PRIVATE).getBoolean("isShowed", false)
            }
            return false
        }

        fun setBank(bank: Bank){
            this.activity?.let {
                val editor = it.getSharedPreferences("bank", Context.MODE_PRIVATE).edit()
                editor.putString("bank_name", bank.name)
                editor.putString("bank_code", bank.code)
                editor.putString("account_name", bank.accountName)
                editor.putString("account_number", bank.accountNumber)

                editor.apply()
            }
        }

        fun getBank():Bank?{
            this.activity?.let {
                val sharedPref = it.getSharedPreferences("bank", Context.MODE_PRIVATE)
                val bank = Bank()
                bank.name = sharedPref.getString("bank_name", "")!!
                bank.code = sharedPref.getString("bank_code", "")!!
                bank.accountName = sharedPref.getString("account_name", "")!!
                bank.accountNumber = sharedPref.getString("account_number", "")!!

                return bank
            }
            return null
        }

        fun setUser(user: User){
            this.activity?.let {
                val editor = it.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                editor.putString("id", user.id)
                editor.putString("name", user.name)
                editor.putString("description", user.description)
                editor.putString("email", user.email)
                editor.putString("imageUrl", user.imageUrl)
                editor.putString("phoneNumber", user.phoneNumber)
                editor.putString("address", user.address)
                editor.putInt("balance", user.balance)
                editor.putString("expire_date", user.expireDate)
                editor.putInt("province_id", user.provinceId)
                editor.putInt("city_id", user.cityId)
                editor.putInt("category_id", user.categoryId)

                editor.apply()
            }
        }

        fun getUser():User?{
            this.activity?.let {
                val sharedPref = it.getSharedPreferences("user", Context.MODE_PRIVATE)
                val user = User()
                user.id = sharedPref.getString("id", "")!!
                user.name = sharedPref.getString("name", "")!!
                user.email = sharedPref.getString("email", "")!!
                user.description = sharedPref.getString("description", "")!!
                user.imageUrl = sharedPref.getString("imageUrl", "")!!
                user.phoneNumber = sharedPref.getString("phoneNumber", "")!!
                user.address = sharedPref.getString("address", "")!!
                user.balance = sharedPref.getInt("balance", 0)
                user.expireDate = sharedPref.getString("expire_date", "")!!
                user.provinceId = sharedPref.getInt("province_id", -1)
                user.cityId = sharedPref.getInt("city_id", -1)
                user.categoryId = sharedPref.getInt("category_id", -1)


                return user
            }
            return null
        }

        fun setToken(token:String?){
            this.activity?.let {
                val editor = it.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                editor.putString("token", token)
                editor.apply()
            }
        }

        fun getToken():String?{
            this.activity?.let {
                return it.getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", null)
            }
            return null
        }
    }

}