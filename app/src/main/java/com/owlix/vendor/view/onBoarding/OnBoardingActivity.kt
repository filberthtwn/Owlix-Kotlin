package com.owlix.vendor.view.onBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.owlix.vendor.R
import com.owlix.vendor.adapter.OnBoardingAdapter
import com.owlix.vendor.view.MainActivity
import com.owlix.vendor.view.auth.AuthActivity
import com.owlix.vendor.view.auth.LoginFragment
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AppCompatActivity(), View.OnClickListener{

    private val slideImages = intArrayOf(
        R.drawable.img_onboarding_first,
        R.drawable.img_onboarding_second
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        this.setupViews()
    }

    private fun setupViews(){

        this.setupViewPager()

        btn_onBoarding_next.setOnClickListener(this)
        btn_onBoarding_skip.setOnClickListener(this)
    }

    private fun setupViewPager(){
        val adapter = OnBoardingAdapter(supportFragmentManager, slideImages)
        vp_onboarding.adapter = adapter

        tl_onBoarding.setupWithViewPager(vp_onboarding)
        tl_onBoarding.clearOnTabSelectedListeners()

        vp_onboarding.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position == (slideImages.size-1)){
                    btn_onBoarding_next.text = "GET STARTED"
                    btn_onBoarding_skip.visibility = View.INVISIBLE
                }else{
                    btn_onBoarding_next.text = "NEXT"
                    btn_onBoarding_skip.visibility = View.VISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }


    override fun onClick(v: View) {
        when(v.id){

            R.id.btn_onBoarding_next -> {
                if (vp_onboarding.currentItem != (slideImages.size-1)){
                    vp_onboarding.currentItem = vp_onboarding.currentItem + 1
                }else{
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            R.id.btn_onBoarding_skip -> {
                vp_onboarding.currentItem = slideImages.size-1
            }

        }
    }

//    on


}