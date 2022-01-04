package com.owlix.vendor.view.main

import android.R.layout
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mevou.customer.ui.dashboard.adapter.MyProductAdapter
import com.owlix.vendor.R
import com.owlix.vendor.constant.Status
import com.owlix.vendor.helper.UserPreferenceHelper
import com.owlix.vendor.view.subscription.SubscriptionActivity
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewInterface.MyProductInterface
import com.owlix.vendor.viewModel.ProductViewModel
import kotlinx.android.synthetic.main.activity_extend_subscription_pop_up.view.*
import kotlinx.android.synthetic.main.fragment_my_product.*


class MyProductFragment : Fragment(), MyProductInterface {

    private var productAdapter:MyProductAdapter? = null
    private var loadingDialog: LoadingDialog? = null
    private val productVM = ProductViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_product, container, false)
    }

    override fun onStart() {
        super.onStart()
        this.setupViews()
        this.setupData()
        this.oberserveViewModel()
    }

    private fun setupViews(){

        productAdapter = MyProductAdapter(arrayListOf(), this,this)
        rv_my_product.adapter = productAdapter
        rv_my_product.layoutManager = LinearLayoutManager(
            this.requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        rv_my_product.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        productAdapter!!.updateData(arrayListOf())
        this.nsv_product.visibility = View.VISIBLE
    }

    private fun setupData(){
        shimmer_my_product_container.startShimmer()
        this.productVM.getAllProduct()
    }

    private fun oberserveViewModel(){
        this.productVM.productResp.observe(viewLifecycleOwner, Observer {
            shimmer_my_product_container.stopShimmer()
            this.nsv_product.visibility = View.INVISIBLE
            it?.let {
                it.products?.let { products ->
                    productAdapter!!.updateData(products)
                }
                this.productVM.productResp.postValue(null)
            }
        })

        this.productVM.baseProductResp.observe(viewLifecycleOwner, Observer { it ->
            this.loadingDialog!!.stopLoading()

            it?.let { response ->

                if (response.status != Status.SUCCESS){
                    productAdapter = MyProductAdapter(arrayListOf(), this,this)
                    rv_my_product.adapter = productAdapter

                    this.nsv_product.visibility = View.VISIBLE
                    this.setupData()
                }

                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                this.productVM.baseProductResp.postValue(null)
            }

        })
    }

    override fun stockUpdate(productId: Int, status:Boolean) {
        super.stockUpdate(productId, status)
        this.loadingDialog = LoadingDialog(childFragmentManager)
        this.loadingDialog!!.startLoading()
//
        this.productVM.updateStock(productId, status)
    }
}