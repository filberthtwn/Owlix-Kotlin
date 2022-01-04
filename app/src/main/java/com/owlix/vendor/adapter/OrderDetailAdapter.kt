package com.mevou.customer.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.owlix.vendor.R
import com.owlix.vendor.data.model.OrderItem
import com.owlix.vendor.data.model.OrderProductItem
import com.owlix.vendor.data.model.Product
import com.owlix.vendor.databinding.OrderDetailItemBinding
import com.owlix.vendor.helper.GlideApp
import com.owlix.vendor.view.order.orderDetail.OrderDetailFragment

class OrderDetailAdapter(
    var data: List<OrderProductItem>,
    private val fragment: Fragment
): RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    fun updateData(data: List<OrderProductItem>, orderDetailFragment: OrderDetailFragment) {
        this.data = data
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<OrderDetailItemBinding>(
            layoutInflator,
            R.layout.item_order_detail,
            parent,
            false
        )
        return ViewHolder(binding)
//        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_order_detail, parent, false))
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = data[position]
        holder.bind(product)
    }

    class ViewHolder(val binding: OrderDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderItem: OrderProductItem) {

            if (orderItem.images.isNotEmpty()){
                GlideApp.with(itemView)
                    .load(orderItem.images.first().image_url)
                    .placeholder(R.drawable.img_square_placeholder)
                    .error(R.drawable.img_square_placeholder)
                    .into(binding.ivOrderDetail)
            }
            binding.orderItem = orderItem
            binding.executePendingBindings()

        }
    }
}