package com.owlix.vendor.viewModel

import androidx.lifecycle.MutableLiveData
import com.owlix.vendor.data.model.NotifResp
import com.owlix.vendor.data.repository.NotifRepo

class NotifViewModel {
    var notificationResp = MutableLiveData<NotifResp>()

    fun getAllNotification() {
        NotifRepo.getInstance().getAllNotification() { isSuccess, response ->
            this.notificationResp.postValue(response)
        }
    }
}