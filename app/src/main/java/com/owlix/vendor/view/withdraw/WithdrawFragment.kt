package com.owlix.vendor.view.withdraw

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import com.owlix.vendor.constant.Status
import com.owlix.vendor.extension.hideKeyboard
import com.owlix.vendor.helper.PriceFormatter
import com.owlix.vendor.helper.UserPreferenceHelper
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.AuthViewModel
import com.owlix.vendor.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_withdraw.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*

class WithdrawFragment : Fragment() {

    private val userVM = UserViewModel()
    private val authVM = AuthViewModel()
    private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_withdraw, container, false)
    }

    override fun onStart() {
        super.onStart()
        this.observeViewModel()

        ll_withdraw.setOnClickListener {
            this.hideKeyboard()
        }

        tb_owlix.tv_tb_title.text = "Withdraw"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        btn_withdraw_add_bank.setOnClickListener {
            findNavController().navigate(R.id.action_withdrawFragment_to_bankFormFragment)
        }

        cl_withdraw_edit.setOnClickListener {
            findNavController().navigate(R.id.action_withdrawFragment_to_bankFormFragment)
        }

        btn_withdraw.setOnClickListener {
            this.loadingDialog = LoadingDialog(childFragmentManager)
            this.loadingDialog!!.startLoading()

            UserPreferenceHelper.getBank()?.let{ bank ->
                this.userVM.createWithdraw(bank, et_withdraw_amount.text.toString().toInt())
            }

        }

    }

    override fun onResume() {
        super.onResume()

        this.setupData()

        UserPreferenceHelper.getBank()?.let {
            if (it.name != ""){
                this.btn_withdraw.isEnabled = true
                this.btn_withdraw.backgroundTintList = ContextCompat.getColorStateList(context!!,
                    R.color.colorPrimary
                )
                this.btn_withdraw.setTextColor(ContextCompat.getColor(context!!, R.color.colorWhite))

                this.cl_withdraw_add_bank.visibility = View.GONE
                this.cl_withdraw_bank_account.visibility = View.VISIBLE

                tv_withdraw_bank_name.text = it.name
                tv_withdraw_account_name.text = it.accountName
                tv_withdraw_account_number.text = it.accountNumber
            }else{
                this.cl_withdraw_input.visibility = View.GONE
                this.btn_withdraw.visibility = View.GONE
            }
        }
    }

    private fun setupData(){
        this.loadingDialog = LoadingDialog(childFragmentManager)
        this.loadingDialog!!.startLoading()

        this.authVM.getUserDetail()
    }

    private fun observeViewModel(){
        this.authVM.userResp.observe(viewLifecycleOwner, Observer {
            this.loadingDialog!!.stopLoading()
            it?.let{ response ->
                if (response.status == Status.SUCCESS){
                    response.data?.let { user ->
                        tv_withdraw_balance.text = PriceFormatter.format(user.balance)
                    }
                }else{
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                this.authVM.userResp.postValue(null)
            }
        })

        this.userVM.withdrawResp.observe(viewLifecycleOwner, Observer {
            this.loadingDialog!!.stopLoading()
            it?.let{ response ->

                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                if (response.status == Status.SUCCESS){
                    findNavController().popBackStack()
                }
                this.authVM.userResp.postValue(null)
            }
        })
    }
}