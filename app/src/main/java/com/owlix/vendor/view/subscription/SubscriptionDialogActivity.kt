package com.owlix.vendor.view.subscription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.owlix.vendor.R
import kotlinx.android.synthetic.main.activity_extend_subscription_pop_up.*

class SubscriptionDialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_dialog)
    }
}