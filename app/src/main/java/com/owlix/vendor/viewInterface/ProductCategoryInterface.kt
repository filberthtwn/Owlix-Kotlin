package com.owlix.vendor.viewInterface

import com.owlix.vendor.data.model.Category
import com.owlix.vendor.data.model.Product

interface ProductCategoryInterface {
    fun onSelected(productCategory: Category)
}