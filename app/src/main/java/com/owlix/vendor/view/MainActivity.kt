package com.owlix.vendor.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.firebase.messaging.FirebaseMessaging
import com.owlix.vendor.R
import com.owlix.vendor.helper.UserPreferenceHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
////                Log.w(TAG)
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            Log.d(TAG, token)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//        })
//        UserPreferenceHelper.activity = this
    }

    override fun onBackPressed() {
        val navHost = NavHostFragment.findNavController(nav_host_fragment).currentDestination!!.id
        if (navHost == R.id.mainFragment){
            super.onBackPressed()
        }else{
            findNavController(R.id.nav_host_fragment).popBackStack()
        }
    }
}



