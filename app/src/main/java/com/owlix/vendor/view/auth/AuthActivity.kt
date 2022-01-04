package com.owlix.vendor.view.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.owlix.vendor.R
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_main.*

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

    }

    override fun onBackPressed() {
        val navHost = NavHostFragment.findNavController(f_auth).currentDestination!!.id
        if (navHost == R.id.loginFragment){
            super.onBackPressed()
        }else{
            findNavController(R.id.f_auth).popBackStack()
        }
    }
}