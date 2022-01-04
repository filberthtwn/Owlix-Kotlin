package com.owlix.vendor.view.auth

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.owlix.vendor.R
import com.owlix.vendor.constant.Status
import com.owlix.vendor.extension.hideKeyboard
import com.owlix.vendor.helper.UserPreferenceHelper
import com.owlix.vendor.view.MainActivity
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.AuthViewModel
import com.owlix.vendor.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_otp.et_otp_first
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*
import java.io.File
import java.io.FileOutputStream
import java.lang.StringBuilder

class OtpFragment : Fragment() {

    private val authVM:AuthViewModel = AuthViewModel()
    private val userVM:UserViewModel = UserViewModel()

    private var phoneNumber:String = ""
    private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.setupViews()
        this.observeViewModel()
    }

    private fun setupViews(){

        arguments?.let {
            OtpFragmentArgs.fromBundle(it).phoneNumber?.let { phoneNumber ->
                this.phoneNumber = phoneNumber
                tv_otp_subtitle.text = getString(R.string.string_otp_subtitle) + " " + phoneNumber
            }
        }

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        tv_otp_resend.setOnClickListener {
            this.loadingDialog = LoadingDialog(childFragmentManager)
            this.loadingDialog!!.startLoading()
            this.authVM.login(this.phoneNumber)
        }

        et_otp_first.requestFocus()

        et_otp_first.addTextChangedListener(this.textWatcher)
        et_otp_second.addTextChangedListener(this.textWatcher)
        et_otp_third.addTextChangedListener(this.textWatcher)
        et_otp_fourth.addTextChangedListener(this.textWatcher)

        btn_verification.setOnClickListener {
            this.loadingDialog = LoadingDialog(childFragmentManager)
            this.loadingDialog!!.startLoading()

            this.verificationProcess()
        }
    }

    private fun observeViewModel(){

        this.authVM.loginResp.observe(viewLifecycleOwner, Observer { loginResp ->

            this.loadingDialog!!.stopLoading()

            loginResp?.let {
//                if (it.status == Status.SUCCESS) {
//                    val action =
//                        LoginFragmentDirections.actionLoginFragmentToOtpFragment("+62" + et_phone_number.text!!.toString())
//                    findNavController().navigate(action)
//                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
//                }
                this.authVM.loginResp.postValue(null)
            }

        })

        this.authVM.verifyResp.observe(viewLifecycleOwner, Observer { loginResp ->

            loginResp?.let {

                this.loadingDialog!!.stopLoading()

                if (it.status == Status.SUCCESS) {
                    UserPreferenceHelper.setToken(loginResp.data)
                    this.authVM.getUserDetail()
                }
//                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                this.authVM.verifyResp.postValue(null)
            }


        })

        this.authVM.userResp.observe(viewLifecycleOwner, Observer { it ->

            this.loadingDialog!!.stopLoading()

            it?.let { userResp ->
                if (userResp.status == Status.SUCCESS) {
                    userResp.data?.let {
                        if (it.onSuspend == "NO"){
                            UserPreferenceHelper.setShowOnBoarding(true)

                            UserPreferenceHelper.setUser(it)
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        Log.w(
                                            ContentValues.TAG,
                                            "Fetching FCM registration token failed",
                                            task.exception
                                        )
                                        return@OnCompleteListener
                                    }

                                    this.userVM.updateFCMToken(task.result!!)
                            })
                        }else{
                            Toast.makeText(context, "Your account is suspended",Toast.LENGTH_LONG).show()
                        }
                    } ?: run {
                        Toast.makeText(context, "User Data Missing.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }

                this.authVM.userResp.postValue(null)
            }

        })

        this.userVM.updateFCMTokenResp.observe(viewLifecycleOwner, Observer { it->
            it?.let {
                if (it.status == Status.SUCCESS) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                this.userVM.updateFCMTokenResp.postValue(null)
            }
        })
    }

    private fun verificationProcess(){

        val otpCode = "" + et_otp_first.text + "" + et_otp_second.text + "" + et_otp_third.text + "" + et_otp_fourth.text
        this.authVM.verifyOTP(this.phoneNumber, otpCode)
    }

    private val textWatcher = object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {

            when(s.hashCode()){
                et_otp_first.text.hashCode() -> {
                    if (s.toString() != "" && s.toString() != " ") {
                        et_otp_second.requestFocus()
                    }
                }

                et_otp_second.text.hashCode() -> {
                    if (s.toString() != "" && s.toString() != " ") {
                        et_otp_third.requestFocus()
                    }
                }

                et_otp_third.text.hashCode() -> {
                    if (s.toString() != "" && s.toString() != " ") {
                        et_otp_fourth.requestFocus()
                    }
                }
            }

        }

    }

}