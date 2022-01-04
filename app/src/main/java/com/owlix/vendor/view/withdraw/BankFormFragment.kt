package com.owlix.vendor.view.withdraw

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import com.owlix.vendor.adapter.BankNameAdapter
import com.owlix.vendor.adapter.ProvinceAdapter
import com.owlix.vendor.constant.Message
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.Bank
import com.owlix.vendor.extension.hideKeyboard
import com.owlix.vendor.helper.UserPreferenceHelper
import com.owlix.vendor.view.auth.RegisterThirdFragmentDirections
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_bank_form.*
import kotlinx.android.synthetic.main.fragment_register_second.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*

class BankFormFragment : Fragment() {

    private val userVM = UserViewModel()
    private var loadingDialog: LoadingDialog? = null

    private var banks: List<Bank> = arrayListOf()
    private var selectedBank:Bank? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bank_form, container, false)
    }

    override fun onStart() {
        super.onStart()

        this.setupViews()
        this.setupData()
        this.observeViewModel()
    }

    private fun setupViews(){
        tb_owlix.tv_tb_title.text = "Input Bank"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        ll_bank_form.setOnClickListener {
            this.hideKeyboard()
        }

        UserPreferenceHelper.getBank()?.let {
            et_withdraw_bank_name.setText(it.name)
            et_withdraw_account_name.setText(it.accountName)
            et_withdraw_account_number.setText(it.accountNumber)
        }

        btn_withdraw_bank.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilih Bank")

            val banks = this.banks
            builder.setAdapter(BankNameAdapter(activity!!, banks)) { dialog, position ->
                this.selectedBank = banks[position]
                et_withdraw_bank_name.setText(banks[position].name)
                dialog.dismiss()
            }

            builder.setCancelable(true)

            val dialog = builder.create()
            dialog.show()

            dialog.setCanceledOnTouchOutside(true)
        }

        btn_withdraw_done.setOnClickListener {

            this.hideKeyboard()

            if (this.validator()){
                val bank = Bank()
                bank.code = selectedBank!!.code
                bank.name = et_withdraw_bank_name.text.toString()
                bank.accountName = et_withdraw_account_name.text.toString()
                bank.accountNumber = et_withdraw_account_number.text.toString()
                UserPreferenceHelper.setBank(bank)

                findNavController().popBackStack()
            }else{
                Toast.makeText(context, Message.Error.FILL_THE_BLANK, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupData(){
        this.loadingDialog = LoadingDialog(childFragmentManager)
        this.loadingDialog!!.startLoading()

        UserPreferenceHelper.getBank()?.let {
            this.selectedBank = it
        }

        this.userVM.getAllBank()
    }

    private fun observeViewModel(){
        this.userVM.bankResp.observe(viewLifecycleOwner, Observer { it ->
            this.loadingDialog!!.stopLoading()

            it?.let { response ->
                if (response.status == Status.SUCCESS){
                    response.data?.let {
                        this.banks = it
                    }
                }else{
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                this.userVM.bankResp.postValue(null)
            }

        })
    }

    private fun validator():Boolean{
        if (et_withdraw_bank_name.text.toString() == ""){
            return false
        }

        if (et_withdraw_account_name.text.toString() == ""){
            return false
        }

        if (et_withdraw_account_number.text.toString() == ""){
            return false
        }

        return true
    }

}