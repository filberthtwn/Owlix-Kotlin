package com.mevou.customer.ui.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.owlix.vendor.R
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.Order
import com.owlix.vendor.databinding.OrderItemBinding
import com.owlix.vendor.helper.GlideApp
import com.owlix.vendor.viewInterface.OrderInterface

class OrderPastAdapter(
    var data: List<Order>,
    val fragment: Fragment
): RecyclerView.Adapter<OrderPastAdapter.ViewHolder>(), OrderInterface {

    fun updateData(data: List<Order>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<OrderItemBinding>(
            layoutInflator,
            R.layout.item_my_order,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.listener = this

        val order = data[position]
        holder.bind(order, fragment.context!!)
    }

    class ViewHolder(val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order, context: Context) {
            binding.order = order

            GlideApp.with(itemView)
                .load(order.imageObjects.first().image_url)
                .error(R.drawable.img_square_placeholder)
                .into(binding.ivOrderImage)

            when (order.status) {
                Status.Order.PAID-> {
                    binding.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                }
                Status.Order.SHIPPING -> {
                    binding.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.colorWarning))
                }
                else -> {
                    binding.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.colorDone))
                }
            }
            binding.executePendingBindings()
        }
    }

    override fun onSelected(order: Order) {
        fragment.findNavController().navigate(R.id.action_mainFragment_to_orderDetailFragment)
    }
}