package com.owlix.vendor.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import com.owlix.vendor.helper.DateFormatter
import com.owlix.vendor.helper.UserPreferenceHelper
import com.owlix.vendor.view.main.MyProductFragment
import com.owlix.vendor.view.main.OrderFragment
import com.owlix.vendor.view.main.ProfileFragment
import com.owlix.vendor.view.subscription.SubscriptionActivity
import kotlinx.android.synthetic.main.activity_extend_subscription_pop_up.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private var currentFragment:Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentFragment = OrderFragment()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        this.setupViews()
        this.setupBottomNavigationBar()
        this.loadFragment()
    }

    override fun onStart() {
        super.onStart()

        UserPreferenceHelper.getUser()?.let {
            val currentDate = DateFormatter.format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"), Date())

            if (currentDate > it.expireDate) {
                this.showSubscribeDialog()
            }
        }
    }

    private fun setupViews(){

        tb_owlix.tv_tb_title.visibility = View.GONE
        tb_owlix.navigationIcon = null

        iv_plus.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToProductFormFragment(false,null)
            findNavController().navigate(action)
        }

        iv_notification.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_notificationListFragment)
        }
    }

    private fun setupBottomNavigationBar() {
        bottom_navigation_bar.setOnNavigationItemSelectedListener {
            iv_plus.visibility = View.GONE
            iv_notification.visibility = View.VISIBLE
            when (it.itemId){
                R.id.nav_order ->
                    currentFragment = OrderFragment()
                R.id.nav_myProduct -> {
                    currentFragment = MyProductFragment()
                }
                R.id.nav_profile ->
                    currentFragment = ProfileFragment()
            }
            this.loadFragment()
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun showSubscribeDialog(){
        var alertDialog: AlertDialog? = null
        val inflater: LayoutInflater = layoutInflater
        val dialogView: View = inflater.inflate(R.layout.activity_extend_subscription_pop_up, null)

        dialogView.btn_extend.setOnClickListener {
            alertDialog!!.dismiss()
            val intent = Intent(activity, SubscriptionActivity::class.java)
            startActivity(intent)
        }

        val builder = AlertDialog.Builder(context, R.style.WrapContentDialog)
        builder.setView(dialogView)

        alertDialog = builder.create()
        alertDialog!!.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show()
    }

    private fun loadFragment(){

        if (currentFragment!!::class == MyProductFragment::class){
            iv_plus.visibility = View.VISIBLE
            iv_notification.visibility = View.GONE
        }

        childFragmentManager.beginTransaction().replace(R.id.fl_main, currentFragment!!).commit()
    }
}