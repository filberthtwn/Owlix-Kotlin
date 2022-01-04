package com.owlix.vendor.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.owlix.vendor.R
import com.owlix.vendor.data.model.Bank
import kotlinx.android.synthetic.main.item_text_list.view.*

//class BankNameAdapter {
//}

class BankNameAdapter(private val context: Activity, private var data: List<Bank>): ArrayAdapter<Bank>(context, R.layout.item_text_list, data) {

    fun updateData(data: List<Bank>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = LayoutInflater.from(context).inflate(R.layout.item_text_list, null, true)
        rowView.tv_title.text = data[position].name
        return rowView
    }
}