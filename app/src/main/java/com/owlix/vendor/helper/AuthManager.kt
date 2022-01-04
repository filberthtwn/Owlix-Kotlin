package com.owlix.vendor.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
//import androidx.core.content.ContextCompat.startActivity
//import com.owlix.vendor.helper.UserPreferenceHelper.Companion.activity
import com.owlix.vendor.view.auth.AuthActivity

class AuthManager {
    companion object{
        fun logout(activity: Activity){
            UserPreferenceHelper.setToken(null)
            val intent = Intent(activity, AuthActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }
}