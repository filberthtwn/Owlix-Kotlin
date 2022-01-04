package com.owlix.vendor.view.editProfile

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import com.owlix.vendor.adapter.CityAdapter
import com.owlix.vendor.adapter.ProvinceAdapter
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.CityData
import com.owlix.vendor.data.model.ProvinceData
import com.owlix.vendor.extension.hideKeyboard
import com.owlix.vendor.helper.GlideApp
import com.owlix.vendor.helper.UserPreferenceHelper
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.AuthViewModel
import com.owlix.vendor.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_edit_personal_information.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_register_second.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class EditPersonalInformationFragment : Fragment() {

    private var imageFile: File? = null
    private var byteArray: ByteArray = byteArrayOf()

    private var loadingDialog: LoadingDialog? = null
    private val userVM: UserViewModel = UserViewModel()
    private val authVM: AuthViewModel = AuthViewModel()

    private var provinces:List<ProvinceData> = arrayListOf()
    private var cities:List<CityData> = arrayListOf()

    private var provinceId:Int? = null
    private var cityId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_personal_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setupData()
    }

    override fun onStart() {
        super.onStart()

        this.setupViews()
        this.observeViewModel()
    }

    private fun setupViews(){
        ll_edit_personal_information.setOnClickListener {
            this.hideKeyboard()
        }

        tb_owlix.tv_tb_title.text = "Personal Information"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        btn_edit_profile_province.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilih Provinsi")

            val provinces = this.provinces
            builder.setAdapter(ProvinceAdapter(activity!!, provinces)) { dialog, position ->
                et_edit_profile_province.setText(provinces[position].name)
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

        btn_edit_profile_city.setOnClickListener {

            this.provinceId?.let {

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Pilih Kota")

                val cities = this.cities
                builder.setAdapter(CityAdapter(activity!!, cities)) { dialog, position ->
                    et_edit_profile_city.setText(cities[position].name)
                    this.cityId = cities[position].id
                    dialog.dismiss()
                }

                builder.setCancelable(true)

                val dialog = builder.create()
                dialog.show()

                dialog.setCanceledOnTouchOutside(true)

            } ?: run{
                Toast.makeText(context,"Pilih Provinsi Terlebih Dahulu", Toast.LENGTH_LONG).show()
            }

        }

        btn_edit_profile_image.setOnClickListener {
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

        btn_edit_profile_done.setOnClickListener {
            this.loadingDialog = LoadingDialog(childFragmentManager)
            this.loadingDialog!!.startLoading()

            val email = this.et_edit_profile_email.text!!.toString()
            val phoneNumber = this.et_edit_profile_phone_number.text!!.toString()
            val address = this.et_edit_profile_address.text!!.toString()

            this.userVM.updateProfile(email, phoneNumber, this.provinceId!!, this.cityId!!, address, imageFile)
        }

    }

    private fun setupData(){
        this.loadingDialog = LoadingDialog(childFragmentManager)
        this.loadingDialog!!.startLoading()

        this.authVM.getAllProvince()
    }

    private fun setupValue(){
        UserPreferenceHelper.getUser()?.let{
            et_edit_profile_email.setText(it.email)
            et_edit_profile_phone_number.setText(it.phoneNumber.replace("+62",""))
            et_edit_profile_address.setText(it.address)

            GlideApp.with(context!!)
                .load(it.imageUrl)
                .error(R.drawable.img_square_placeholder)
                .into(iv_edit_profile_image)

            this.provinces.forEach {province ->
                if (it.provinceId == province.id){
                    this.provinceId = province.id
                    et_edit_profile_province.setText(province.name)
                    this.authVM.getAllCity(province.id)
                    return
                }
            }
        }
    }

    private fun setupCityValue(){
        UserPreferenceHelper.getUser()?.let{
            this.cities.forEach { city ->
                if (it.cityId == city.id){
                    this.cityId = city.id
                    et_edit_profile_city.setText(city.name)
                    return
                }
            }
        }
    }

    private fun observeViewModel() {
        this.authVM.userResp.observe(viewLifecycleOwner, Observer {
            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    response.data?.let { user ->

                        UserPreferenceHelper.setUser(user)
                        this.setupValue()

                    }
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                this.authVM.userResp.postValue(null)
            }
        })

        this.authVM.provinceResp.observe(viewLifecycleOwner, Observer { provinceResp ->

            provinceResp?.let { response ->
                if (response.status == Status.SUCCESS) {
                    this.provinces = provinceResp.data
                    this.authVM.getUserDetail()
                    this.authVM.provinceResp.postValue(null)
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
                this.authVM.provinceResp.postValue(null)
            }
        })

        this.authVM.cityResp.observe(viewLifecycleOwner, Observer { cityResp ->
            this.loadingDialog!!.stopLoading()

            cityResp?.let { response ->
                if (response.status == Status.SUCCESS) {
                    this.cities = cityResp.data
                    this.setupCityValue()
                    this.authVM.cityResp.postValue(null)
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
                this.authVM.cityResp.postValue(null)
            }

        })

        this.userVM.updatePersonalInfoResp.observe(viewLifecycleOwner, Observer { it ->
            this.loadingDialog!!.stopLoading()

            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    findNavController().popBackStack()
                }
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                this.userVM.updatePersonalInfoResp.postValue(null)
            }

        })

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

                iv_edit_profile_image.setImageURI(data?.data)
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