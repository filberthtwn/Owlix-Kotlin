package com.owlix.vendor.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.HandlerCompat.postDelayed
import com.owlix.vendor.R
import com.owlix.vendor.helper.UserPreferenceHelper
import com.owlix.vendor.view.auth.AuthActivity
import com.owlix.vendor.view.onBoarding.OnBoardingActivity

class LaunchScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 3000 // 1 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_screen)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        Handler().postDelayed({
            var intent = Intent(this, OnBoardingActivity::class.java)

            println(UserPreferenceHelper.getToken())

            UserPreferenceHelper.activity = this

            if (UserPreferenceHelper.getShowOnBoarding()){
                if (UserPreferenceHelper.getToken() != null) {
                    intent = Intent(this, MainActivity::class.java)
                }else{
                    intent = Intent(this, AuthActivity::class.java)
                }
            }

            startActivity(intent)
            finish()

        }, SPLASH_TIME_OUT)
    }
}