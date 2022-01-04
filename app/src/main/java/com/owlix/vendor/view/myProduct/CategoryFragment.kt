package com.owlix.vendor.view.myProduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.owlix.vendor.R
import com.owlix.vendor.adapter.CategoryAdapter
import com.owlix.vendor.constant.Status
import com.owlix.vendor.data.model.Category
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*
import java.util.*

class CategoryConstant{
    companion object{
        var selectedProductCategory:Category? = null
    }
}

class CategoryFragment : Fragment() {

    private val productVM = ProductViewModel()
    private var loadingDialog: LoadingDialog? = null

    private var adapter:CategoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onStart() {
        super.onStart()

        this.setupViews()
        this.setupData()
        this.observeViewModel()
    }

    private fun setupViews(){
        tb_owlix.tv_tb_title.text = "Pilih Kategori"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        adapter = CategoryAdapter(arrayListOf(),this)
        rv_category.adapter = adapter
        rv_category.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_category.setHasFixedSize(true)
    }

    private fun setupData(){
        this.loadingDialog = LoadingDialog(childFragmentManager)
        this.loadingDialog!!.startLoading()
        this.productVM.getAllCategory()
    }

    private fun observeViewModel(){
        this.productVM.categoryResp.observe(viewLifecycleOwner, { it ->
            this.loadingDialog!!.stopLoading()
            it?.let {
                if (it.status == Status.SUCCESS){
                    adapter!!.updateData(it.data)
                }else{
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                this.productVM.categoryResp.postValue(null)
//                this.categories = it.data
//                this.productVM.getProductDetail(this.productId.toInt())
            }
        })
    }

}