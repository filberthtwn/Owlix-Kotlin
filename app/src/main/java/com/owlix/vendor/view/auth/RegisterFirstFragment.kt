package com.owlix.vendor.view.auth

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import com.owlix.vendor.extension.hideKeyboard
import kotlinx.android.synthetic.main.fragment_register_first.*

class RegisterFirstFragment : Fragment() {

    private var isPasswordSeen:Boolean = false
    private var isConfirmPasswordSeen:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_first, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ll_signup_first.setOnClickListener {
            this.hideKeyboard()
        }

        tv_signup_login.setOnClickListener {
            findNavController().popBackStack()
        }

        btn_signup_next.setOnClickListener {
            findNavController().navigate(R.id.action_registerFirstFragment_to_registerSecondFragment)
        }

//        iv_signup_password_eye.setOnClickListener {
//            if (isPasswordSeen){
//                iv_signup_password_eye.setImageResource(R.drawable.ic_eye_solid)
//                et_signup_phone_number.transformationMethod = PasswordTransformationMethod()
//            }else{
//                iv_signup_password_eye.setImageResource(R.drawable.ic_eye_slash)
//                et_signup_phone_number.transformationMethod = null
//            }
//            this.isPasswordSeen = !this.isPasswordSeen
//        }

//        iv_signup_confirm_password_eye.setOnClickListener {
//            if (isConfirmPasswordSeen){
//                iv_signup_confirm_password_eye.setImageResource(R.drawable.ic_eye_solid)
//                et_signup_confirm_password.transformationMethod = PasswordTransformationMethod()
//            }else{
//                iv_signup_confirm_password_eye.setImageResource(R.drawable.ic_eye_slash)
//                et_signup_confirm_password.transformationMethod = null
//            }
//            this.isConfirmPasswordSeen = !this.isConfirmPasswordSeen
//        }
    }
}