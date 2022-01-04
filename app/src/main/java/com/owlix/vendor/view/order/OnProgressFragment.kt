package com.owlix.vendor.view.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mevou.customer.ui.dashboard.adapter.OrderAdapter
import com.owlix.vendor.R
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.Product
import com.owlix.vendor.helper.AuthManager
import com.owlix.vendor.view.MainFragmentDirections
import com.owlix.vendor.viewModel.OrderViewModel
import kotlinx.android.synthetic.main.fragment_my_product.*
import kotlinx.android.synthetic.main.fragment_on_progress.*

class OnProgressFragment : Fragment() {

    private var adapter:OrderAdapter? = null
    private val orderVM = OrderViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_progress, container, false)
    }

    override fun onStart() {
        super.onStart()
        this.setupViews()
        this.setupData()
        this.observeViewModel()
    }

    private fun setupViews(){
        adapter = OrderAdapter(arrayListOf(), this)
        rv_order_in_progress.adapter = adapter
        rv_order_in_progress.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_order_in_progress.setHasFixedSize(true)
    }

    private fun setupData(){
        shimmer_order_onprogress_container.startShimmer()
        this.orderVM.getAllOrder(Status.Order.SHIPPING)
    }

    private fun observeViewModel(){
        this.orderVM.orderResp.observe(viewLifecycleOwner, Observer {

            shimmer_order_onprogress_container.stopShimmer()
            this.nsv_order_onprogress.visibility = View.INVISIBLE

            it?.let{
                when (it.status) {
                    Status.SUCCESS -> {
                        it.orders?.let {
                            this.adapter!!.updateData(it)
                        }
                    }
                    Status.UNAUTHORIZED -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        AuthManager.logout(activity!!)
                    }
                    else -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                this.orderVM.orderResp.postValue(null)
            }
        })
    }

}