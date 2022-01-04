package com.owlix.vendor.view.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.*
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.vending.licensing.AESObfuscator
import com.owlix.vendor.R
import com.owlix.vendor.constant.Status
import com.owlix.vendor.helper.DateFormatter
import com.owlix.vendor.helper.GlideApp
import com.owlix.vendor.helper.PriceFormatter
import com.owlix.vendor.helper.UserPreferenceHelper
import com.owlix.vendor.view.auth.AuthActivity
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private val authVM = AuthViewModel()
    private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onStart() {
        super.onStart()
        this.setupViews()
        this.setupData()
        this.observeViewModel()
    }


    private fun setupValue(){
        UserPreferenceHelper.getUser()?.let{
            tv_profile_name.text = it.name
            tv_profile_balance.text = PriceFormatter.format(it.balance)

            GlideApp.with(context!!)
                .load(it.imageUrl)
                .error(R.drawable.img_square_placeholder)
                .into(iv_profile_image)

            val currentDate = DateFormatter.format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"), Date())

            if (currentDate > it.expireDate) {
                tv_profile_subscription_date.text = "Until: Expired"
            } else {
                val parser = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val expireDate = DateFormatter.format("dd MMMM yyyy", null, parser.parse(it.expireDate))
                tv_profile_subscription_date.text = "Until: $expireDate"
            }
        }
    }

    private fun setupViews(){

        this.setupValue()

        btn_profile_balance.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_withdrawFragment)
        }

        btn_profile_edit_profile.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_editProfileFragment)
        }

        btn_profile_withdraw_history.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_withdrawHistoryFragment)
        }

        btn_profile_about_us.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_aboutUsFragment)
        }

        btn_profile_terms_condition.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_termsConditionFragment)
        }

        btn_profile_faq.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_faqFragment)
        }

        btn_profile_privacy_policy.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_privacyPolicyFragment)
        }

        tv_renew_subscription.setOnClickListener {

//            billingClient.startConnection(object : BillingClientStateListener{
//                override fun onBillingSetupFinished(billingResult: BillingResult) {
//                    if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
////                        val flowParams = BillingFlowParams.newBuilder()
////                            .setSkuDetails(querySkuDetails())
////                            .build()
//                        // The BillingClient is ready. You can query purchases here.
//                    }
//                }
//
//                override fun onBillingServiceDisconnected() {
//
//                }
//            })
        }

        btn_profile_logout.setOnClickListener {
            this.loadingDialog = LoadingDialog(childFragmentManager)
            this.loadingDialog!!.startLoading()

            this.authVM.logout()
        }
    }

//    suspend fun querySkuDetails()- {
//        val skuList = ArrayList<String>()
//        skuList.add("premium_upgrade")
//        skuList.add("gas")
//        val params = SkuDetailsParams.newBuilder()
//        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
//        withContext(Dispatchers.IO) {
//            billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
//                // Process the result.
//            }
//        }
//    }

    private fun setupData(){


        this.loadingDialog = LoadingDialog(childFragmentManager)
        this.loadingDialog!!.startLoading()

        this.authVM.getUserDetail()
    }

    private fun observeViewModel(){

        this.authVM.userResp.observe(viewLifecycleOwner, Observer {
            this.loadingDialog!!.stopLoading()
            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    response.data?.let { user ->
                        if (user.onSuspend == "NO"){
                            UserPreferenceHelper.setUser(user)
                            this.setupValue()
                        }else{
                            Toast.makeText(context, "Your account is suspended", Toast.LENGTH_LONG).show()
                            this.loadingDialog = LoadingDialog(childFragmentManager)
                            this.loadingDialog!!.startLoading()
                            this.authVM.logout()
                        }
                    }
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                this.authVM.userResp.postValue(null)
            }
        })

        this.authVM.loginResp.observe(viewLifecycleOwner, Observer {
            this.loadingDialog!!.stopLoading()
            it?.let {
                if (it.status == Status.SUCCESS){
                    UserPreferenceHelper.setToken(null)
                    val intent = Intent(context!!, AuthActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                }
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                this.authVM.loginResp.postValue(null)
            }
        })
    }

}