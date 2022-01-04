package com.owlix.vendor.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.owlix.vendor.R
import kotlinx.android.synthetic.main.fragment_faq.*
import kotlinx.android.synthetic.main.fragment_privacy_policy.*
import kotlinx.android.synthetic.main.toolbar_owlix.*
import kotlinx.android.synthetic.main.toolbar_owlix.view.*

class FaqFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onStart() {
        super.onStart()
        this.setupViews()
    }

    private fun setupViews(){
        tb_owlix.tv_tb_title.text = "Pusat Bantuan"
        tb_owlix.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        wv_faq.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl("https://production.owlix-id.com/html/FAQ.html")
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        wv_faq.loadUrl("https://production.owlix-id.com/html/FAQ.html")
    }
}