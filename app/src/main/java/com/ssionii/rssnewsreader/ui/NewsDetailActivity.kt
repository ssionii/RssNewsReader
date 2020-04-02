package com.ssionii.rssnewsreader.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ssionii.rssnewsreader.R
import com.ssionii.rssnewsreader.data.News
import com.ssionii.rssnewsreader.databinding.ActivityNewsDetailBinding

class NewsDetailActivity : AppCompatActivity(){

    lateinit var viewDataBinding : ActivityNewsDetailBinding
    lateinit var news : News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail)

        setSupportActionBar(viewDataBinding.actNewsDetailToolbar)

        news = intent.getSerializableExtra("newsData") as News

        viewDataBinding.newsDetailActivity = this

        setWebView(news.url)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun setWebView(url: String){
        viewDataBinding.actWebWv.apply{
            settings.javaScriptEnabled = true
            loadUrl(url)

            webChromeClient = WebChromeClient()
            webViewClient = WebViewCLientClass()


        }

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && viewDataBinding.actWebWv.canGoBack()) {
            viewDataBinding.actWebWv.goBack();
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    inner class WebViewCLientClass : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {

            viewDataBinding.actNewsDetailPb.visibility = VISIBLE

            Log.e("check URL", url)
            view!!.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {

            viewDataBinding.actNewsDetailPb.visibility = INVISIBLE

            super.onPageFinished(view, url)
        }
    }

}