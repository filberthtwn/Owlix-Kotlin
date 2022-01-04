package com.mevou.customer.ui.dashboard.myOrder.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val fragmentList: MutableList<Fragment> = arrayListOf()
    private val titleList:MutableList<String> = arrayListOf()

    override fun getCount(): Int {
        return fragmentList.count()
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    fun addFragment(fragment:Fragment, title:String){
        fragmentList.add(fragment)
        titleList.add(title)
    }
}