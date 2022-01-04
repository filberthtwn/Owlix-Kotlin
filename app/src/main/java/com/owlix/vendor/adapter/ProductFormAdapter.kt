package com.mevou.customer.ui.dashboard.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.owlix.vendor.R
import com.owlix.vendor.data.model.ImageObject
import com.owlix.vendor.helper.GlideApp
import com.owlix.vendor.viewInterface.MyProductInterface
import kotlinx.android.synthetic.main.item_filled_product_image.view.*
import java.io.File


class ProductFormAdapter(
    var imagesUrl:MutableList<ImageObject>,
    var delegate:MyProductInterface,
    private val fragment: Fragment
): RecyclerView.Adapter<ProductFormAdapter.ViewHolder>() {

    fun updateData(imagesUrl:MutableList<ImageObject>) {
        this.imagesUrl = imagesUrl
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_filled_product_image,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return this.imagesUrl.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindContent(this.imagesUrl[position].image_url, position)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bindContent(imageUrl: String, idx:Int) {

            view.ll_product_remove_image.setOnClickListener {
                delegate.onImageRemoved(idx, false)
            }

            GlideApp.with(itemView)
                .load(imageUrl)
                .placeholder(R.drawable.img_square_placeholder)
                .error(R.drawable.img_square_placeholder)
                .into(view.iv_product_image)
        }
    }
}