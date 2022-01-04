package com.owlix.vendor.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import com.owlix.vendor.constant.Message
import com.owlix.vendor.constant.Status
import com.owlix.vendor.extension.hideKeyboard
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private var isPasswordSeen: Boolean = false
    private var loadingDialog:LoadingDialog? = null
    private val authVM: AuthViewModel = AuthViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.setupViews()
        this.observeViewModel()
    }

    private fun setupViews() {

        ll_login.setOnClickListener {
            this.hideKeyboard()
        }

        tv_login_signup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btn_send_otp.setOnClickListener {

            if (et_phone_number.text!!.toString() != ""){
                this.loadingDialog = LoadingDialog(childFragmentManager)
                this.loadingDialog!!.startLoading()
                this.authVM.login("+62" + et_phone_number.text!!.toString())
            }else{
                Toast.makeText(context, Message.Error.FILL_THE_BLANK, Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun observeViewModel() {

        this.authVM.loginResp.observe(viewLifecycleOwner, Observer { loginResp ->

            this.loadingDialog!!.stopLoading()

            loginResp?.let {
                if (it.status == Status.SUCCESS) {
                    val action =
                        LoginFragmentDirections.actionLoginFragmentToOtpFragment("+62" + et_phone_number.text!!.toString())
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                this.authVM.loginResp.postValue(null)
            }

        })
    }
}