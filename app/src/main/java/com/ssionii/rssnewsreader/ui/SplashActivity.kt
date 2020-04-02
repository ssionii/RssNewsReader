package com.ssionii.rssnewsreader.ui

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ssionii.rssnewsreader.MainActivity
import com.ssionii.rssnewsreader.R
import com.ssionii.rssnewsreader.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity(){

    lateinit var viewDataBinding : ActivitySplashBinding

    var versionName =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setVersion()

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewDataBinding.splashActivity = this

        setUI()

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1300)

    }

    private fun setVersion(){
        var pInfo: PackageInfo? = null
        try {
            pInfo = packageManager.getPackageInfo(
                this.packageName, 0
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        versionName = pInfo!!.versionName
    }

    private fun setUI(){
        viewDataBinding.actSplashIvLogo.apply{
            background = resources.getDrawable(R.drawable.bg_oval)
            clipToOutline = true
        }
    }
}