package com.owlix.vendor.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.owlix.vendor.R
import com.owlix.vendor.data.model.Product
import com.owlix.vendor.data.model.ProvinceData
import kotlinx.android.synthetic.main.item_text_list.view.*

class ProvinceAdapter(private val context: Activity, private var data: List<ProvinceData>): ArrayAdapter<ProvinceData>(context, R.layout.item_text_list, data) {

    fun updateData(data: List<ProvinceData>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = LayoutInflater.from(context).inflate(R.layout.item_text_list, null, true)
        rowView.tv_title.text = data[position].name
        return rowView
    }
}