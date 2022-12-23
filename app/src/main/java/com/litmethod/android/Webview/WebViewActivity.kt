package com.litmethod.android.Webview

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebViewClient
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityWebViewBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.utlis.AppConstants.Companion.WEB_URL

class WebViewActivity : BaseActivity() {
    lateinit var binding: ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {

        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }


        var url = intent?.getStringExtra(WEB_URL)!!
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(url)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(false)

    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack())
            binding.webView.goBack()
        else
            super.onBackPressed()
    }
}