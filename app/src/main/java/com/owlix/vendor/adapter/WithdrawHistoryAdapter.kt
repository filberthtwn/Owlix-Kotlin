package com.owlix.vendor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.owlix.vendor.R
import com.owlix.vendor.data.model.Order
import com.owlix.vendor.data.model.WithdrawHistory
import com.owlix.vendor.databinding.WithdrawHistoryBinding
import com.owlix.vendor.helper.DateFormatter
import java.text.SimpleDateFormat
import java.util.*

class WithdrawHistoryAdapter(
    var data: List<WithdrawHistory>,
    val fragment: Fragment
): RecyclerView.Adapter<WithdrawHistoryAdapter.ViewHolder>()  {
    fun updateData(data: List<WithdrawHistory>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<WithdrawHistoryBinding>(
            layoutInflator,
            R.layout.item_withdraw_history,
            parent,
            false
        )
        return ViewHolder(binding)
//        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_withdraw_history, parent, false))
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.binding.listener = this

        val order = data[position]
        holder.bind(order)
    }

    class ViewHolder(val binding: WithdrawHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(withdrawHistory:WithdrawHistory) {
            binding.withdrawHistory = withdrawHistory


            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = DateFormatter.format("dd MMM yyyy", TimeZone.getDefault(), parser.parse(withdrawHistory.createdAt))
            val time = DateFormatter.format("HH:mm", TimeZone.getDefault(), parser.parse(withdrawHistory.createdAt))

            binding.tvWithdrawHistoryDate.text = date
            binding.tvWithdrawHistoryTime.text = time
            binding.executePendingBindings()
        }
    }
}