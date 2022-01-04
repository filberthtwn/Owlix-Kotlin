package com.owlix.vendor.view.auth

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import com.owlix.vendor.adapter.CityAdapter
import com.owlix.vendor.adapter.PartnerCategoryAdapter
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.PartnerCategoryData
import com.owlix.vendor.data.model.PartnerCategoryResp
import com.owlix.vendor.extension.hideKeyboard
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_register_second.*
import kotlinx.android.synthetic.main.fragment_register_third.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*

class RegisterThirdFragment : Fragment() {

    val authVM = AuthViewModel()
    private var loadingDialog: LoadingDialog? = null

    private var categories: List<PartnerCategoryData> = arrayListOf()
    private var selectedCategory: PartnerCategoryData? = null

    private var phoneNumber:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_third, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        this.setupData()
        this.observeViewModel()

        tb_owlix.tv_tb_title.text = "Sign Up"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        ll_signup_third.setOnClickListener {
            this.hideKeyboard()
        }

        btn_signup_category.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilih Kategori")

            val categories = categories
            builder.setAdapter(PartnerCategoryAdapter(activity!!, categories)) { dialog, position ->
                et_signup_category.setText(categories[position].name)
                this.selectedCategory = categories[position]
                dialog.dismiss()
            }

            builder.setCancelable(true)

            val dialog = builder.create()
            dialog.show()

            dialog.setCanceledOnTouchOutside(true)
        }

        btn_signup_open_store.setOnClickListener {

            this.loadingDialog = LoadingDialog(childFragmentManager)
            this.loadingDialog!!.startLoading()

            arguments?.let {
                val bundle = RegisterThirdFragmentArgs.fromBundle(it)
                val email = bundle.email
                val phoneNumber = bundle.phoneNumber
                this.phoneNumber = phoneNumber
                val address = bundle.address
                val provinceId = bundle.provinceId
                val cityId = bundle.cityId
                val imageFile = bundle.imageFile

                val storeName = et_signup_store_name.text.toString()
                val biodata = et_signup_biodata.text.toString()

                this.selectedCategory?.let { it ->
                    this.authVM.signup(
                        name = storeName,
                        email = email,
                        phoneNumber = phoneNumber,
                        provinceId = provinceId,
                        cityId = cityId,
                        address = address,
                        categoryId = it.id,
                        imageFile = imageFile
                    )
                }
            }
        }

//            val intent = Intent(context!!, RegisterSuccessActivity::class.java)
//            startActivity(intent)
//            activity!!.finish()
    }

    override fun onResume() {
        super.onResume()
        this.setupData()
    }

    private fun setupData() {
        this.loadingDialog = LoadingDialog(childFragmentManager)
        this.loadingDialog!!.startLoading()
        this.authVM.getAllCategory()
    }


    private fun observeViewModel() {
        this.authVM.partnerCategoryResp.observe(viewLifecycleOwner, Observer { it ->

            it?.let {
                this.categories = it.data
                this.loadingDialog!!.stopLoading()
                this.authVM.partnerCategoryResp.postValue(null)
            }

        })

        this.authVM.verifyResp.observe(viewLifecycleOwner, Observer { it ->

            it?.let {
                this.loadingDialog!!.stopLoading()
                if (it.status == Status.SUCCESS){
                    val action = RegisterThirdFragmentDirections.actionRegisterSecondFragmentToOtpFragment(this.phoneNumber)
                    findNavController().navigate(action)
                }
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                this.authVM.verifyResp.postValue(null)
            }

        })
    }

}