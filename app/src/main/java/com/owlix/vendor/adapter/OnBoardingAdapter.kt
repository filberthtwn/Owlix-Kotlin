package com.owlix.vendor.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.owlix.vendor.view.onBoarding.OnBoardingFragment

class OnBoardingAdapter(manager: FragmentManager, private var slideImages:IntArray) : FragmentPagerAdapter(manager) {

    override fun getCount(): Int = slideImages.size

    override fun getItem(position: Int): Fragment {
        return OnBoardingFragment(slideImages[position])
    }
}