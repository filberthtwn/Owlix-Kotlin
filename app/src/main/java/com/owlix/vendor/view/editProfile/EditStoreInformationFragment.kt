package com.owlix.vendor.view.editProfile

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
import com.owlix.vendor.adapter.PartnerCategoryAdapter
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.PartnerCategoryData
import com.owlix.vendor.data.model.User
import com.owlix.vendor.helper.UserPreferenceHelper
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.AuthViewModel
import com.owlix.vendor.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_edit_store_information.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*

class EditStoreInformationFragment : Fragment() {

    private var loadingDialog: LoadingDialog? = null
    private val userVM: UserViewModel = UserViewModel()
    private val authVM: AuthViewModel = AuthViewModel()

    private var user: User? = null

    private var categories: List<PartnerCategoryData> = arrayListOf()
    private var selectedCategory: PartnerCategoryData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_store_information, container, false)
    }

    override fun onStart() {
        super.onStart()

        this.setupViews()
        this.setupData()
        this.observeViewModel()
    }

    private fun setupViews() {
        tb_owlix.tv_tb_title.text = "Store Information"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        btn_edit_profile_done.setOnClickListener {
            this.loadingDialog = LoadingDialog(childFragmentManager)
            this.loadingDialog!!.startLoading()

            this.user?.let{ user ->
                user.name = et_edit_store_name.text.toString()
                user.description = et_edit_store_biodata.text.toString()
                this.selectedCategory?.let{ category ->
                    user.categoryId = category.id
                }

                this.authVM.updateStoreInfo(user)

            }
        }

        btn_edit_store_category.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilih Kategori")

            val categories = categories
            builder.setAdapter(PartnerCategoryAdapter(activity!!, categories)) { dialog, position ->
                et_edit_store_category.setText(categories[position].name)
                this.selectedCategory = categories[position]
                dialog.dismiss()
            }

            builder.setCancelable(true)

            val dialog = builder.create()
            dialog.show()

            dialog.setCanceledOnTouchOutside(true)
        }
    }

    private fun setupData() {
        this.loadingDialog = LoadingDialog(childFragmentManager)
        this.loadingDialog!!.startLoading()

        this.authVM.getAllCategory()
    }

    private fun setupValue() {
        UserPreferenceHelper.getUser()?.let {

            et_edit_store_name.setText(it.name)
            et_edit_store_biodata.setText(it.description)

            this.categories.forEach { category ->
                if (it.categoryId == category.id) {
                    this.selectedCategory = category
                    et_edit_store_category.setText(category.name)
                    return
                }
            }

        }
    }

    private fun observeViewModel() {
        this.authVM.partnerCategoryResp.observe(viewLifecycleOwner, Observer {

            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    this.categories = it.data
                    this.authVM.getUserDetail()
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }

                this.authVM.partnerCategoryResp.postValue(null)
            }

        })
        this.authVM.userResp.observe(viewLifecycleOwner, Observer {
            this.loadingDialog!!.stopLoading()
            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    response.data?.let { user ->

                        this.user = user

                        UserPreferenceHelper.setUser(user)
                        this.setupValue()

                    }
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                this.authVM.userResp.postValue(null)
            }
        })

        this.authVM.updateStoreInfoResp.observe(viewLifecycleOwner, Observer {
            this.loadingDialog!!.stopLoading()
            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    findNavController().popBackStack()
//                    response.data?.let { user ->

//                        this.user = user
//
//                        UserPreferenceHelper.setUser(user)
//                        this.setupValue()

//                    }
                }
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                this.authVM.updateStoreInfoResp.postValue(null)
            }
        })
    }
}