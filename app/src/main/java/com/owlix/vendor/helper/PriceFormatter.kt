package com.owlix.vendor.helper

class PriceFormatter {
    companion object{
        fun format(price:Int):String{
            val priceString = String.format("Rp %,d", price).replace(',', '.')
            return priceString
        }
    }

}