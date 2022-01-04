package com.owlix.vendor.view.notification

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
import com.owlix.vendor.adapter.NotificationAdapter
import com.owlix.vendor.constant.Status
import com.owlix.vendor.helper.AuthManager
import com.owlix.vendor.viewModel.NotifViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_notification_list.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*

class NotificationListFragment : Fragment() {

    private var adapter:NotificationAdapter? = null
    private var notifVM:NotifViewModel = NotifViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        this.setupViews()
        this.setupData()
        this.observeViewModel()
    }

    private fun setupViews(){

        tb_owlix.tv_tb_title.text = "Notifications"
        tb_owlix.setOnClickListener {
            findNavController().popBackStack()
        }

        adapter = NotificationAdapter(arrayListOf(), this)
        rv_notifications.adapter = adapter
        rv_notifications.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_notifications.setHasFixedSize(true)
    }

    private fun setupData(){
        ll_shimmer.visibility = View.VISIBLE
        shimmer_notif_container.startShimmer()

        this.notifVM.getAllNotification()
    }

    private fun observeViewModel(){
        this.notifVM.notificationResp.observe(viewLifecycleOwner, Observer {

            ll_shimmer.visibility = View.INVISIBLE
            shimmer_notif_container.stopShimmer()

            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    this.adapter!!.updateData(response.notifications)
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                this.notifVM.notificationResp.postValue(null)
            }
        })
    }

}