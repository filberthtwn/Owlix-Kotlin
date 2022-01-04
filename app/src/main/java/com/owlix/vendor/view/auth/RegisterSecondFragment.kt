package com.owlix.vendor.view.auth

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import com.owlix.vendor.adapter.CityAdapter
import com.owlix.vendor.adapter.ProvinceAdapter
import com.owlix.vendor.constant.Message
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.CityData
import com.owlix.vendor.data.model.ProvinceData
import com.owlix.vendor.extension.hideKeyboard
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register_second.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder


class RegisterSecondFragment : Fragment() {

    private var imageFile: File? = null
    private var byteArray: ByteArray = byteArrayOf()

    private var loadingDialog: LoadingDialog? = null
    private val authVM:AuthViewModel = AuthViewModel()

    private var provinces:List<ProvinceData> = arrayListOf()
    private var cities:List<CityData> = arrayListOf()

    private var provinceId:Int? = null
    private var cityId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_second, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.setupData()
        this.observeViewModel()


        tb_owlix.tv_tb_title.text = "Sign Up"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        ll_signup_second.setOnClickListener {
            this.hideKeyboard()
        }

        val html = StringBuilder()
        html.append("Saya setuju dengan <a href=\\\"launch.ltactivity://OpenPage?page=register\\\">Syarat dan Ketentuan</a> dan Kebijakan Privasi Owlix")
        tv_signup_disclaimer.text = Html.fromHtml(html.toString())
        tv_signup_disclaimer.movementMethod = LinkMovementMethod.getInstance()

        btn_signup_province.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilih Provinsi")

            val provinces = this.provinces
            builder.setAdapter(ProvinceAdapter(activity!!, provinces)) { dialog, position ->
                et_signup_province.setText(provinces[position].name)
                this.provinceId = provinces[position].id
                dialog.dismiss()

                this.loadingDialog = LoadingDialog(childFragmentManager)
                this.loadingDialog!!.startLoading()

                this.authVM.getAllCity(provinces[position].id)
            }

            builder.setCancelable(true)

            val dialog = builder.create()
            dialog.show()

            dialog.setCanceledOnTouchOutside(true)
        }

        btn_signup_city.setOnClickListener {

            this.provinceId?.let {

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Pilih Kota")

                val cities = this.cities
                builder.setAdapter(CityAdapter(activity!!, cities)) { dialog, position ->
                    et_signup_city.setText(cities[position].type+" "+cities[position].name)
                    this.cityId = cities[position].id
                    dialog.dismiss()
                }

                builder.setCancelable(true)

                val dialog = builder.create()
                dialog.show()

                dialog.setCanceledOnTouchOutside(true)

            } ?: run{
                Toast.makeText(context,"Pilih Provinsi Terlebih Dahulu",Toast.LENGTH_LONG).show()
            }

        }

        btn_signup_image.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, 1001)

                } else {
                    EasyImage.openGallery(this, 0)
                }
            }
        }

        btn_register_next.setOnClickListener {

            if (this.validator()) {
                val email = et_signup_email.text!!.toString()
                val phoneNumber = "+62" + et_signup_phone_number.text!!.toString()
                val address = et_signup_address.text!!.toString()

                val action = RegisterSecondFragmentDirections.actionRegisterSecondFragmentToRegisterThirdFragment(email, phoneNumber, this.provinceId!!, this.cityId!!, imageFile, address)
                findNavController().navigate(action)
            }else{
                Toast.makeText(context, Message.Error.FILL_THE_BLANK, Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun setupData(){
        this.loadingDialog = LoadingDialog(childFragmentManager)
        this.loadingDialog!!.startLoading()
        this.authVM.getAllProvince()
    }

    private fun observeViewModel() {
        this.authVM.provinceResp.observe(viewLifecycleOwner, Observer { provinceResp ->

            provinceResp?.let { response ->
                if (response.status == Status.SUCCESS) {
                    this.provinces = provinceResp.data
                    this.provinceId = this.provinces[0].id

                    et_signup_province.setText(this.provinces[0].name)
                    this.authVM.getAllCity(provinces[0].id)
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
                this.authVM.provinceResp.postValue(null)
            }

        })

        this.authVM.cityResp.observe(viewLifecycleOwner, Observer { cityResp ->

            cityResp?.let {response ->
                if (response.status == Status.SUCCESS) {
                    this.loadingDialog!!.stopLoading()
                    this.cities = cityResp.data
                    this.cityId = this.cities[0].id

                    et_signup_city.setText(this.cities[0].type+ " " + this.cities[0].name)
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
                this.authVM.cityResp.postValue(null)
            }

        })
    }

    private fun validator():Boolean{
        if (et_signup_email.text.isNullOrBlank()){
            return false
        }

        if (et_signup_phone_number.text.isNullOrBlank()){
            return false
        }

        if (et_signup_province.text.isNullOrBlank()){
            return false
        }

        if (et_signup_city.text.isNullOrBlank()){
            return false
        }


        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(), object :
            EasyImage.Callbacks {
            override fun onImagesPicked(
                p0: MutableList<File>,
                p1: EasyImage.ImageSource?,
                p2: Int
            ) {
                imageFile = p0[0]

                /* Compress Bitmap */
                val originalBitmap = BitmapFactory.decodeFile(imageFile!!.path)
                val baos = ByteArrayOutputStream()
                originalBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos)
                byteArray = baos.toByteArray()

                iv_signup_image.setImageURI(data?.data)
            }

            override fun onImagePickerError(p0: Exception?, p1: EasyImage.ImageSource?, p2: Int) {
                println("ERROR")
            }

            override fun onCanceled(p0: EasyImage.ImageSource?, p1: Int) {
                println("CANCELED")
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1001 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    EasyImage.openGallery(this, 0)
                } else {
                    Toast.makeText(context, "Permission denied to read your External storage",Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

}