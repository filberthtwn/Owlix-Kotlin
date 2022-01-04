package com.owlix.vendor.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.owlix.vendor.R
import com.owlix.vendor.view.MainActivity
import kotlinx.android.synthetic.main.activity_register_success.*

class RegisterSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_success)

        this.setupViews()
    }

    private fun setupViews(){
        btn_signup_ok.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}