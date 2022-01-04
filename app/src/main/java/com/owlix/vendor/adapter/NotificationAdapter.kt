package com.owlix.vendor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.owlix.vendor.R
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.Notification
import com.owlix.vendor.data.model.Order
import com.owlix.vendor.databinding.NotifItemBinding
import com.owlix.vendor.databinding.OrderItemBinding
import com.owlix.vendor.helper.GlideApp
import com.owlix.vendor.viewInterface.NotifInterface
import com.owlix.vendor.viewInterface.OrderInterface

class NotificationAdapter(
    var data: List<Notification>,
    val fragment: Fragment
): RecyclerView.Adapter<NotificationAdapter.ViewHolder>(), NotifInterface {

    fun updateData(data: List<Notification>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<NotifItemBinding>(
            layoutInflator,
            R.layout.item_notification,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.listener = this

        val notif = data[position]
        holder.bind(notif, fragment.context!!)
    }

    class ViewHolder(val binding: NotifItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notif: Notification, context: Context) {
            binding.notif = notif
            binding.executePendingBindings()
        }
    }

    override fun onNotifSelected(notif: Notification) {
        super.onNotifSelected(notif)
        println("ABC")
    }
}