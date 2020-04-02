package com.ssionii.rssnewsreader.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssionii.rssnewsreader.R
import com.ssionii.rssnewsreader.data.News
import com.ssionii.rssnewsreader.databinding.ItemNewsBinding

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