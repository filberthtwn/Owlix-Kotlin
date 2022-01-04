package com.owlix.vendor.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import com.owlix.vendor.extension.hideKeyboard
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*

class EditProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onStart() {
        super.onStart()
        this.setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.hideKeyboard()
    }

    private fun setupViews(){
        tb_owlix.tv_tb_title.text = "Edit Profile"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

//        btn_bank_account.setOnClickListener {
//            findNavController().navigate(R.id.action_editProfileFragment_to_bankAccountFormFragment)
//        }

        btn_edit_profile_personal_information.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_editPersonalInformationFragment)
        }

        btn_edit_profile_store_information.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_editStoreInformationFragment)
        }

        ll_edit_profile.setOnClickListener {
            this.hideKeyboard()
        }

    }


}