package com.owlix.vendor.view.withdrawHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.owlix.vendor.R
import com.owlix.vendor.adapter.WithdrawHistoryAdapter
import com.owlix.vendor.constant.Status
import com.owlix.vendor.view.templates.LoadingDialog
import com.owlix.vendor.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_withdraw_completed.*
import kotlinx.android.synthetic.main.fragment_withdraw_in_progress.*

class WithdrawInProgressFragment : Fragment() {

    private val userVM = UserViewModel()
    private var adapter: WithdrawHistoryAdapter? = null
    private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_withdraw_in_progress, container, false)
    }

    override fun onStart() {
        super.onStart()
        this.setupViews()
        this.setupData()
        this.observeViewModel()
    }

    private fun setupViews(){
        adapter = WithdrawHistoryAdapter(arrayListOf(), this)
        rv_withdraw_in_progress.adapter = adapter
        rv_withdraw_in_progress.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_withdraw_in_progress.setHasFixedSize(true)
    }

    private fun setupData(){
        this.loadingDialog = LoadingDialog(childFragmentManager)
        this.loadingDialog!!.startLoading()
        this.userVM.getAllWithdraw(Status.Withdraw.WAITING)
    }

    private fun observeViewModel(){
        this.userVM.withdrawHistoryResp.observe(viewLifecycleOwner, Observer { response ->
            this.loadingDialog!!.stopLoading()

            response?.let{
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { data ->
                            this.adapter!!.updateData(data)
                        }
                    }
                    else -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                this.userVM.withdrawHistoryResp.postValue(null)
            }
        })
    }

}