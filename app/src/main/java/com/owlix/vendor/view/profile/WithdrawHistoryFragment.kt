package com.owlix.vendor.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mevou.customer.ui.dashboard.myOrder.adapter.ViewPagerAdapter
import com.owlix.vendor.R
import com.owlix.vendor.view.order.HistoryFragment
import com.owlix.vendor.view.order.NewOrderFragment
import com.owlix.vendor.view.order.OnProgressFragment
import com.owlix.vendor.view.withdrawHistory.WithdrawCompletedFragment
import com.owlix.vendor.view.withdrawHistory.WithdrawInProgressFragment
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_withdraw_history.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*

class WithdrawHistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_withdraw_history, container, false)
    }

    override fun onStart() {
        super.onStart()

        tb_owlix.tv_tb_title.text = "Withdraw History"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(WithdrawInProgressFragment(), "In Progress")
        adapter.addFragment(WithdrawCompletedFragment(), "Completed")
        vp_withdraw_history.adapter = adapter
        tl_withdraw_history.setupWithViewPager(vp_withdraw_history)
    }

}