package com.owlix.vendor.view.myProduct

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mevou.customer.ui.dashboard.adapter.ProductFormAdapter
import com.mevou.customer.ui.dashboard.adapter.ProductFormLocalAdapter
import com.owlix.vendor.R
import com.owlix.vendor.constant.Message
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.*
import com.owlix.vendor.extension.hideKeyboard
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewInterface.MyProductInterface
import com.owlix.vendor.viewModel.AuthViewModel
import com.owlix.vendor.viewModel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_product_form.*
import kotlinx.android.synthetic.main.fragment_register_second.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception

class ProductFormFragment : Fragment(), MyProductInterface {

    private val productVM = ProductViewModel()

    private var categories: List<Category> = arrayListOf()

    private var selectedProduct:Product? = null
    private var loadingDialog: LoadingDialog? = null

    private var productId:String = ""

    private var images:MutableList<File> = arrayListOf()
    private var imagesUrl:MutableList<ImageObject> = arrayListOf()

    private var adapter:ProductFormAdapter? = null
    private var adapterLocal:ProductFormLocalAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setupViews()
        if (CategoryConstant.selectedProductCategory == null){
            this.setupData()
        }else{
            if (images.size > 0){
                rv_product_form_local.visibility = View.VISIBLE
            }
            this.setupDefaultValue()
        }

        this.observeViewModel()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.hideKeyboard()
    }

    private fun setupViews(){

        tb_owlix.tv_tb_title.text = "Tambah Produk"
        tb_owlix.setNavigationOnClickListener {
            CategoryConstant.selectedProductCategory = null
            findNavController().popBackStack()
        }

        btn_product_detail_delete.visibility = View.GONE

        // SETUP PRODUCT LOCAL RECYCLER VIEW
        this.adapterLocal = ProductFormLocalAdapter(this.images, this, this)
        rv_product_form_local.adapter = this.adapterLocal
        rv_product_form_local.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_product_form_local.setHasFixedSize(true)

        // SETUP PRODUCT ONLINE RECYCLER VIEW
        this.adapter = ProductFormAdapter(this.imagesUrl, this, this)
        rv_product_form.adapter = this.adapter
        rv_product_form.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_product_form.setHasFixedSize(true)

        btn_product_form_category.setOnClickListener {
            findNavController().navigate(R.id.action_productFormFragment_to_categoryFragment)
        }

        ll_product_form.setOnClickListener {
            this.hideKeyboard()
        }

        et_product_weight.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (et_product_weight.text.toString() == ""){
                    tv_product_form_gram.visibility = View.GONE
                    val density = context!!.resources.displayMetrics.density
                    et_product_weight.setPadding(0,0, (16*density).toInt(),0)
                }else{
                    tv_product_form_gram.visibility = View.VISIBLE
                    et_product_weight.setPadding(0,0,0,0)
                }

            }

            override fun afterTextChanged(s: Editable?) {}

        })

        btn_product_save.setOnClickListener {
            if (this.validator()){
                this.loadingDialog = LoadingDialog(childFragmentManager)
                this.loadingDialog!!.startLoading()

                val product = Product()
                product.name = et_product_name.text.toString()
                product.description = et_product_description.text.toString()
                product.weight = et_product_weight.text.toString()
                product.price = et_product_price.text.toString().toInt()
                product.categoryId = CategoryConstant.selectedProductCategory!!.id
                product.images = this.imagesUrl

                this.selectedProduct?.let {
                    product.id = it.id
                    this.productVM.updateProduct(product, this.images)
                } ?: run{
                    this.productVM.createProduct(product, this.images)
                }

            }else{
                Toast.makeText(context, Message.Error.FILL_THE_BLANK, Toast.LENGTH_LONG).show()
            }
        }

        iv_product_form_add_photo.setOnClickListener {
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
    }

    private fun setupData(){

        arguments?.let {
            ProductFormFragmentArgs.fromBundle(it).productId?.let { productId ->
                this.tb_owlix.tv_tb_title.text = "Edit Produk"
                this.productId = productId
                this.loadingDialog = LoadingDialog(childFragmentManager)

                btn_product_detail_delete.visibility = View.VISIBLE

                btn_product_detail_delete.setOnClickListener {
                    this.loadingDialog = LoadingDialog(childFragmentManager)
                    this.loadingDialog!!.startLoading()
                    this.productVM.deleteProduct(this.productId.toInt())
                }

                this.loadingDialog!!.startLoading()
                this.productVM.getAllCategory()
            }
        }

    }

    private fun validator():Boolean{
        if (et_product_name.text.toString() == ""){
            return false
        }

        if (et_product_description.text.toString() == ""){
            return false
        }

        if (et_product_price.text.toString() == ""){
            return false
        }

        if (et_product_weight.text.toString() == ""){
            return false
        }

        if (CategoryConstant.selectedProductCategory == null){
            return false
        }

        return true
    }

    private fun setupDefaultValue(){
        this.selectedProduct?.let{ product ->
            et_product_name.setText(product.name)
            et_product_description.setText(product.description)

            et_product_price.setText(product.price.toString())
            et_product_weight.setText(product.weight)

            this.adapter!!.updateData(product.images.toMutableList())
        }

        CategoryConstant.selectedProductCategory?.let{ productCategory ->
            tv_product_category_name.text = productCategory.name
            tv_product_category_name.visibility = View.VISIBLE
        }

    }

    private fun observeViewModel(){

        this.productVM.categoryResp.observe(viewLifecycleOwner, { it ->

            it?.let {
                if (it.status == Status.SUCCESS){
                    this.categories = it.data
                    this.productVM.getProductDetail(this.productId.toInt())
                }else{
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                this.productVM.categoryResp.postValue(null)
            }

        })

        this.productVM.productDetailResp.observe(viewLifecycleOwner, {
            this.loadingDialog!!.stopLoading()
            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    response.product?.let { product ->
                        this.selectedProduct = product
                        this.imagesUrl = this.selectedProduct!!.images.toMutableList()

                        CategoryConstant.selectedProductCategory = this.categories.first { x -> x.id == product.categoryId }

                        this.setupDefaultValue()
                    }
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                this.productVM.productDetailResp.postValue(null)
            }
        })

        this.productVM.baseProductResp.observe(viewLifecycleOwner, {
            this.loadingDialog!!.stopLoading()

            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    CategoryConstant.selectedProductCategory = null
                    findNavController().popBackStack()
                }
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                this.productVM.baseProductResp.postValue(null)
            }

        })

        this.productVM.deleteProductImageResp.observe(viewLifecycleOwner, {
            this.loadingDialog!!.stopLoading()

            it?.let { response ->
                if (response.status == Status.SUCCESS) {
                    this.adapter!!.updateData(this.imagesUrl)
                }
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                this.productVM.baseProductResp.postValue(null)
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
                this@ProductFormFragment.images.add(p0[0])
                rv_product_form_local.visibility = View.VISIBLE
                this@ProductFormFragment.adapterLocal!!.updateData(this@ProductFormFragment.images)
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

    override fun onImageRemoved(idx: Int, isLocal:Boolean) {
        super.onImageRemoved(idx, isLocal)

        if (isLocal){
            this.images.removeAt(idx)
            if (images.size == 0){
                rv_product_form_local.visibility = View.GONE
            }
            this.adapterLocal!!.updateData(this.images)
        }else{
            this.loadingDialog = LoadingDialog(childFragmentManager)
            this.loadingDialog!!.startLoading()

            this.productVM.deleteProductImage(this.imagesUrl[idx].id)
            this.imagesUrl.removeAt(idx)
        }

    }
}