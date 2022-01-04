package com.owlix.vendor.view.order.orderDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mevou.customer.ui.dashboard.adapter.OrderDetailAdapter
import com.owlix.vendor.R
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.CityData
import com.owlix.vendor.data.model.ProvinceData
import com.owlix.vendor.data.model.User
import com.owlix.vendor.extension.hideKeyboard
import com.owlix.vendor.helper.DateFormatter
import com.owlix.vendor.helper.PriceFormatter
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.AuthViewModel
import com.owlix.vendor.viewModel.OrderViewModel
import kotlinx.android.synthetic.main.fragment_input_receipt_number_dialog.*
import kotlinx.android.synthetic.main.fragment_order_detail.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailFragment : Fragment() {

    private var selectedOrderId:Int = -1
    private var adapter:OrderDetailAdapter? = null

    private var loadingDialog: LoadingDialog? = null
    private var authVM:AuthViewModel = AuthViewModel()
    private var orderVM:OrderViewModel = OrderViewModel()

    private var buyer: User? = null
    private var provinces:List<ProvinceData> = arrayListOf()
    private var cities:List<CityData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }


    override fun onStart() {
        super.onStart()
        this.setupViews()
        this.setupData()
        this.observeViewModel()
    }

    private fun setupViews(){
        tb_owlix.tv_tb_title.text = "Order Detail"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        adapter = OrderDetailAdapter(arrayListOf(), this)
        rv_order_detail.adapter = adapter
        rv_order_detail.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_order_detail.setHasFixedSize(true)

        nsv_order_detail.visibility = View.INVISIBLE
        ll_order_detail_button.visibility = View.INVISIBLE

        include_input_receipt.alpha = 0.0f

        btn_order_detail_reject.setOnClickListener {
            this.loadingDialog = LoadingDialog(childFragmentManager)
            this.loadingDialog!!.startLoading()
            this.orderVM.rejectOrder(this.selectedOrderId)
        }

        btn_order_detail_proceed.setOnClickListener {

            include_input_receipt.visibility = View.VISIBLE
            et_receipt_code.requestFocus()

            v_input_receipt.setOnClickListener {
                this.hideReceiptInputView()
            }

            btn_close.setOnClickListener {
                this.hideReceiptInputView()
            }

            btn_confirm.setOnClickListener {
                this.loadingDialog = LoadingDialog(childFragmentManager)
                this.loadingDialog!!.startLoading()
                if (et_receipt_code.text.toString() != ""){
                    this.orderVM.updateReceiptCode(this.selectedOrderId, et_receipt_code.text.toString())
                }
                this.hideReceiptInputView()
            }

            include_input_receipt.animate().alpha(1.0f).duration = 100
        }


    }

    private fun setupData(){
        this.loadingDialog = LoadingDialog(childFragmentManager)
        this.loadingDialog!!.startLoading()

        this.authVM.getAllProvince()
    }

    private fun observeViewModel(){


        this.authVM.provinceResp.observe(viewLifecycleOwner, Observer { provinceResp ->

            provinceResp?.let { response ->
                if (response.status == Status.SUCCESS) {
                    this.provinces = provinceResp.data

                    arguments?.let {
                        this.selectedOrderId = OrderDetailFragmentArgs.fromBundle(it).orderId
                        this.orderVM.getOrderDetail(this.selectedOrderId)
                    }

                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
                this.authVM.provinceResp.postValue(null)
            }

        })

        this.authVM.cityResp.observe(viewLifecycleOwner, Observer { cityResp ->
            this.loadingDialog!!.stopLoading()

            cityResp?.let { response ->
                if (response.status == Status.SUCCESS) {
                    this.cities = response.data

                    nsv_order_detail.visibility = View.VISIBLE

                    this.buyer?.let{ buyer ->
                        tv_detail_order_customer_name.text = buyer.name
                        tv_detail_order_customer_phone.text = buyer.phoneNumber
                        tv_detail_order_customer_address.text = buyer.address
                        tv_detail_order_customer_postalCode.text = buyer.postalCode

                        this.provinces.forEach { province ->
                            if (province.id == buyer.provinceId){
                                tv_detail_order_customer_province.text = province.name
                            }
                        }

                        this.cities.forEach { city ->
                            if (city.id == buyer.cityId){
                                tv_detail_order_customer_city.text = city.name
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
                this.authVM.cityResp.postValue(null)
            }

        })

        this.orderVM.orderDetailResp.observe(viewLifecycleOwner, {
            it?.let { response ->
                if (response.status == Status.SUCCESS) {

                    response.data?.let { order ->
                        tv_order_detail_invoiceId.text = order.invoiceId

                        val parser =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        val datetime = DateFormatter.format(
                            "dd MMM yyyy, HH:mm",
                            TimeZone.getDefault(),
                            parser.parse(order.createdAt)
                        )
                        tv_order_detail_datetime.text = datetime

                        if (order.status != Status.Order.PAID && order.status != Status.Order.NEW) {
                            ll_order_detail_button.visibility = View.GONE
                            cl_order_detail.setPadding(0, 0, 0, 48)
                        } else {
                            ll_order_detail_button.visibility = View.VISIBLE
                        }

                        when (order.status) {
                            Status.Order.PAID-> {
                                tv_order_detail_status.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                            }
                            Status.Order.SHIPPING -> {
                                tv_order_detail_status.setTextColor(ContextCompat.getColor(context!!, R.color.colorWarning))
                            }
                            else -> {
                                tv_order_detail_status.setTextColor(ContextCompat.getColor(context!!, R.color.colorDone))
                            }
                        }
                        tv_order_detail_status.text = order.status

//                        tv_order_detail_subtotal_store.text = PriceFormatter.format(order. - order.deliveryFee)
                        var subtotal = 0
                        var storeSubtotal = 0
                        order.orderItem.forEach { orderItem ->
                            subtotal += (orderItem.price * orderItem.quantity)
                            storeSubtotal += (orderItem.storePrice * orderItem.quantity)
                        }
                        tv_order_detail_subtotal_store.text = PriceFormatter.format(storeSubtotal)
                        tv_order_detail_subtotal.text =  PriceFormatter.format(subtotal)

                        tv_order_detail_shipping_fee.text = PriceFormatter.format(order.deliveryFee)

                        tv_detail_order_total_store.text = PriceFormatter.format(storeSubtotal + order.deliveryFee)
                        tv_detail_order_total.text = PriceFormatter.format(subtotal + order.deliveryFee)

                        order.buyer?.let { buyer ->
                            this.buyer = buyer
                            this.authVM.getAllCity(buyer.provinceId)
                        }

                        adapter!!.updateData(order.orderItem, this)

                    }

                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                this.orderVM.orderDetailResp.postValue(null)
            }
        })

        this.orderVM.updateReceiptCodeResp.observe(viewLifecycleOwner, {
            this.loadingDialog!!.stopLoading()
            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                this.orderVM.updateReceiptCodeResp.postValue(null)
            }
        })

        this.orderVM.rejectOrderResp.observe(viewLifecycleOwner, {
            this.loadingDialog!!.stopLoading()
            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    findNavController().popBackStack()
                }
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                this.orderVM.rejectOrderResp.postValue(null)
            }
        })
    }

    private fun hideReceiptInputView(){
        include_input_receipt.animate().alpha(0.0f).setDuration(100).withEndAction {
            include_input_receipt.visibility = View.GONE
        }.start()
        this.hideKeyboard()
    }
}