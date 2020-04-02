package com.ssionii.rssnewsreader.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssionii.rssnewsreader.R
import com.ssionii.rssnewsreader.data.News
import com.ssionii.rssnewsreader.databinding.ActivityMainBinding
import com.ssionii.rssnewsreader.databinding.ItemNewsBinding
import kotlinx.android.synthetic.main.item_news.view.*
import java.lang.Exception

class NewsRecyclerViewAdapter : RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>(){

    private var listener: OnNewsItemClickListener? = null

    private var itemList = mutableListOf<News>()
    fun setOnNewsItemClickListener(listener: OnNewsItemClickListener) {
        this.listener = listener
    }

    fun replaceAll(items : MutableList<News>){
        itemList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding =
            DataBindingUtil.inflate<ItemNewsBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_news, parent, false)

        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount(): Int{
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewDataBiding.news = itemList[position]
        holder.viewDataBiding.root.setOnClickListener{
            listener?.onItemClickListener(itemList[position])
        }
    }


    inner class ViewHolder(val viewDataBiding: ItemNewsBinding)
        : RecyclerView.ViewHolder(viewDataBiding.root) {
    }

    interface OnNewsItemClickListener{
        fun onItemClickListener(item: News)
    }

}