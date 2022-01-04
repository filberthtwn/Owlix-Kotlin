package com.owlix.vendor.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mevou.customer.ui.dashboard.myOrder.adapter.ViewPagerAdapter
import com.owlix.vendor.R
import com.owlix.vendor.view.order.HistoryFragment
import com.owlix.vendor.view.order.NewOrderFragment
import com.owlix.vendor.view.order.OnProgressFragment
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onStart() {
        super.onStart()
        this.setupViews()
    }

    private fun setupViews(){

//        val adapter = OrderAdapter()
//        adapter.adapter = this.restoAdapter
//        adapter.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
//        adapter.setHasFixedSize(true)



        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(NewOrderFragment(), "New")
        adapter.addFragment(OnProgressFragment(), "On Progress")
        adapter.addFragment(HistoryFragment(), "Past")
        vp_order.adapter = adapter
        tl_order.setupWithViewPager(vp_order)
    }
}