package com.owlix.vendor.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.owlix.vendor.R
import com.owlix.vendor.data.model.CityData
import com.owlix.vendor.data.model.Product
import com.owlix.vendor.data.model.ProvinceData
import kotlinx.android.synthetic.main.item_text_list.view.*

class CityAdapter(private val context: Activity, private var data: List<CityData>): ArrayAdapter<CityData>(context, R.layout.item_text_list, data) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = LayoutInflater.from(context).inflate(R.layout.item_text_list, null, true)
        rowView.tv_title.text = "" + data[position].type + " " + data[position].name
        return rowView
    }

}