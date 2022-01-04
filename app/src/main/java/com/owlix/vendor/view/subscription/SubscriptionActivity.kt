package com.owlix.vendor.view.subscription

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.billingclient.api.*
import com.owlix.vendor.R
import com.owlix.vendor.helper.DateFormatter
import com.owlix.vendor.helper.UserPreferenceHelper
import com.owlix.vendor.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_subscription.*
import java.util.*

class SubscriptionActivity : AppCompatActivity() {

    private var userVM = UserViewModel()
    private var billingClient:BillingClient? = null

    private val purchasesUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK){
                UserPreferenceHelper.getUser()?.let {
                    val cal = Calendar.getInstance()
                    cal.add(Calendar.MONTH, 1)
                    val newDate = DateFormatter.format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"), cal.time)
                    userVM.updateSubscription(newDate)
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        this.setupViews()
        this.requestPayment()
        this.observeViewModel()
    }

    private fun setupViews(){
        iv_close.setOnClickListener {
            this.finish()
        }

        btn_maybe_later.setOnClickListener {
            this.finish()
        }
    }

    private fun observeViewModel(){
        this.userVM.updateSubscriptionResp.observe(this, Observer { it ->
            it?.let{

                UserPreferenceHelper.getUser()?.let { user ->
                    user.expireDate = it.data
                    UserPreferenceHelper.setUser(user)
                }

                this.finish()
                this.userVM.updateSubscriptionResp.postValue(null)
            }
        })
    }

    private fun requestPayment() {

        this.billingClient = BillingClient.newBuilder(this)
            .setListener(this.purchasesUpdateListener)
            .enablePendingPurchases()
            .build()

        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    this@SubscriptionActivity.querySkuDetails()
                }
            }
            override fun onBillingServiceDisconnected() {
                println("DISCONNECTED")
            }
        })
    }

    private fun querySkuDetails() {
        val skuList = ArrayList<String>()
        skuList.add("owlix_subs")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        billingClient!!.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            println(skuDetailsList)
            skuDetailsList?.let {
                btn_subscribe.text = "Subscribe - " + skuDetailsList.first().price + " /Bulan"
                this.btn_subscribe.setOnClickListener {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.first())
                        .build()
                    val responseCode =
                        billingClient!!.launchBillingFlow(this, flowParams).debugMessage
                }
            } ?: run {
                Toast.makeText(this.applicationContext, "No Subscription Package", Toast.LENGTH_SHORT).show()
            }
        }
    }
}