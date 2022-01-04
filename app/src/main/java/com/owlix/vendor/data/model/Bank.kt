package com.owlix.vendor.data.model

import com.google.gson.annotations.SerializedName


class BankResp {
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data:List<Bank>? = null
}

class Bank {
    @SerializedName("name")
    var name: String = ""

    @SerializedName("code")
    var code: String = ""

    var accountName: String = ""
    var accountNumber: String = ""

}

class WithdrawResp {
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

//    @SerializedName("data")
//    var data:List<Bank>? = null
}

class WithdrawHistoryResp {
    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data:List<WithdrawHistory>? = null
}

class WithdrawHistory{
    @SerializedName("created_at")
    var createdAt: String = ""

    @SerializedName("amount")
    var amount: Int = 0

    @SerializedName("bank_code")
    var bankCode: String = ""

    @SerializedName("account_holder_name")
    var accountName: String = ""

    @SerializedName("account_number")
    var accountNumber: String = ""
}