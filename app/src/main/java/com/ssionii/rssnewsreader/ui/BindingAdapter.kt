package com.ssionii.rssnewsreader.ui

import android.view.View.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssionii.rssnewsreader.R
import com.ssionii.rssnewsreader.data.News

@BindingAdapter("glideImg")
fun ImageView.setGlideImg(url: String){
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .error(R.drawable.img_default)
        .into(this)
}


@BindingAdapter("replaceAll")
fun RecyclerView.replaceAll(list: ArrayList<News>){
    this.adapter
}

@BindingAdapter("llVisibilityByString")
fun LinearLayout.setVisibilityByString(str: String){
    if(str == "")
        this.visibility = GONE
    else
        this.visibility = VISIBLE
}

@BindingAdapter("versionName")
fun TextView.setVersionName(version: String){
    this.text = "v " + version
}