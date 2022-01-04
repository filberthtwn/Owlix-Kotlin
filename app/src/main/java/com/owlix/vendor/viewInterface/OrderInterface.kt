package com.owlix.vendor.viewInterface

import com.owlix.vendor.data.model.Order


interface OrderInterface {
    fun onSelected(order: Order)
}