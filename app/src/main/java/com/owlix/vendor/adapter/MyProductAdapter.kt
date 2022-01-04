package com.mevou.customer.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.owlix.vendor.R
import com.owlix.vendor.data.model.Product
import com.owlix.vendor.databinding.ProductItemBinding
import com.owlix.vendor.helper.GlideApp
import com.owlix.vendor.view.MainFragmentDirections
import com.owlix.vendor.viewInterface.MyProductInterface

class MyProductAdapter(
    var data: List<Product>,
    private val delegate: MyProductInterface,
    private val fragment: Fragment
): RecyclerView.Adapter<MyProductAdapter.ViewHolder>(), MyProductInterface {

    fun updateData(data: List<Product>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ProductItemBinding>(
            layoutInflator,
            R.layout.item_my_product,
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

        val product = data[position]
        holder.bind(product, delegate)
    }

    class ViewHolder(val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product, delegate:MyProductInterface) {

            if (product.images.isNotEmpty()){
                GlideApp.with(itemView)
                    .load(product.images.first().image_url)
                    .error(R.drawable.img_square_placeholder)
                    .into(binding.ivProductImage)
            }

            binding.swProduct.isChecked = product.inStock

            binding.swProduct.setOnCheckedChangeListener { _, isChecked ->
                delegate.stockUpdate(product.id, !product.inStock)
            }

            binding.product = product
//            binding.executePendingBindings()
        }
    }

    override fun onProductSelected(product: Product) {
        val action = MainFragmentDirections.actionMainFragmentToProductFormFragment(true, product.id.toString())
        fragment.findNavController().navigate(action)
    }
}