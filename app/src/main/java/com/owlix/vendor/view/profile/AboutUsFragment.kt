package com.owlix.vendor.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*

class AboutUsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onStart() {
        super.onStart()
        this.setupViews()
    }

    private fun setupViews(){
        tb_owlix.tv_tb_title.text = "Tentang Kami"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

}