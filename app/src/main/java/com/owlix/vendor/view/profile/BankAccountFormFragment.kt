package com.owlix.vendor.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import com.owlix.vendor.helper.UserPreferenceHelper
import kotlinx.android.synthetic.main.fragment_bank_form.*
import kotlinx.android.synthetic.main.fragment_withdraw.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*

class BankAccountFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bank_account_form, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tb_owlix.tv_tb_title.text = "Rekening Bank"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


    }
}