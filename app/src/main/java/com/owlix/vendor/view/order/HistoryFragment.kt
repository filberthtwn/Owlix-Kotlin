package com.owlix.vendor.view.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mevou.customer.ui.dashboard.adapter.OrderAdapter
import com.owlix.vendor.R
import com.owlix.vendor.constant.Status
import com.owlix.vendor.helper.AuthManager
import com.owlix.vendor.viewModel.OrderViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_on_progress.*

class HistoryFragment : Fragment() {

    private var adapter:OrderAdapter? = null
    private val orderVM = OrderViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onStart() {
        super.onStart()
        this.setupViews()
        this.setupData()
        this.observeViewModel()
    }

    private fun setupViews(){
        adapter = OrderAdapter(arrayListOf(), this)
        rv_order_history.adapter = adapter
        rv_order_history.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_order_history.setHasFixedSize(true)
    }

    private fun setupData(){
        shimmer_order_past_container.startShimmer()
        this.orderVM.getAllOrder(Status.Order.COMPLETED)
    }

    private fun observeViewModel(){
        this.orderVM.orderResp.observe(viewLifecycleOwner, Observer {

            shimmer_order_past_container.stopShimmer()
            this.nsv_order_past.visibility = View.INVISIBLE

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