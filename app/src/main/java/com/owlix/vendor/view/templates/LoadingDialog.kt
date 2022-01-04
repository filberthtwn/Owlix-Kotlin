package com.owlix.vendor.view.templates

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.owlix.vendor.R


class LoadingDialog(private val fm: FragmentManager):DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.semi_rounded_button)
        return inflater.inflate(R.layout.loading_dialog, container)
    }

    fun startLoading(){
        this.isCancelable = false
        this.show(fm, "loadingDialog")
    }

    fun stopLoading(){
        this.dismiss()
    }

}