package com.owlix.vendor.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mevou.customer.ui.dashboard.adapter.OrderAdapter
import com.owlix.vendor.R
import com.owlix.vendor.data.model.Category
import com.owlix.vendor.data.model.WithdrawHistory
import com.owlix.vendor.databinding.ProductCategoryBinding
import com.owlix.vendor.helper.DateFormatter
import com.owlix.vendor.view.myProduct.CategoryConstant
import com.owlix.vendor.view.myProduct.CategoryFragment
import com.owlix.vendor.viewInterface.ProductCategoryInterface
import java.text.SimpleDateFormat
import java.util.*

class CategoryAdapter(
    var data: List<Category>,
    val fragment: Fragment
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(), ProductCategoryInterface {

    fun updateData(data: List<Category>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ProductCategoryBinding>(
            layoutInflator,
            R.layout.item_category,
            parent,
            false
        )
        return ViewHolder(binding)
//        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.listener = this

        val productCategory = data[position]
        holder.bind(productCategory)
    }

    class ViewHolder(val binding: ProductCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productcategory: Category) {
            binding.productCategory = productcategory
            binding.executePendingBindings()
        }
    }

    override fun onSelected(productCategory: Category) {
        CategoryConstant.selectedProductCategory = productCategory
        fragment.findNavController().popBackStack()
    }
}