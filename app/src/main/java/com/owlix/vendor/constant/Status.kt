package com.owlix.vendor.constant

class Status {
    companion object{
        val SUCCESS = "success"
        val UNAUTHORIZED = "Unauthorized"
    }

    class Order{
        companion object{
            val NEW = "NEW"
            val PAID = "PAID"
            val SHIPPING = "SHIPPING"
            val COMPLETED = "COMPLETED"
        }
    }

    class Withdraw{
        companion object{
            val WAITING = "WAITING"
            val COMPLETED = "COMPLETED"
        }

    }
}