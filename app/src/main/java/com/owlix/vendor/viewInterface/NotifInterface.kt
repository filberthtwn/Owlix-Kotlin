package com.owlix.vendor.viewInterface

import com.owlix.vendor.data.model.Notification

interface NotifInterface {
    fun onNotifSelected(notif: Notification){}
}