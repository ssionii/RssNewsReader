package com.ssionii.rssnewsreader.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ssionii.rssnewsreader.R
import com.ssionii.rssnewsreader.data.News
import com.ssionii.rssnewsreader.databinding.ActivityMainBinding
import com.ssionii.rssnewsreader.ui.NewsDetailActivity

class MainActivity : AppCompatActivity() {

    lateinit var viewDataBinding : ActivityMainBinding
    val viewModel = NewsMainViewModel(this)

    lateinit var newsRecyclerViewAdapter: NewsRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        viewDataBinding.mainActivity = this
        viewDataBinding.vm = viewModel

        setRv()
        refreshRv()
        setProgressBar()
    }

    private fun setRv(){
        newsRecyclerViewAdapter = NewsRecyclerViewAdapter()
        newsRecyclerViewAdapter.setOnNewsItemClickListener(onItemClickListener)

        viewDataBinding.actMainRv.apply {
            adapter = newsRecyclerViewAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)

        }
    }

    val onItemClickListener = object :
        NewsRecyclerViewAdapter.OnNewsItemClickListener {

        override fun onItemClickListener(item: News) {
            val intent = Intent(this@MainActivity, NewsDetailActivity::class.java)
            intent.putExtra("newsData", item as News)

            startActivity(intent)
        }
    }

    private fun refreshRv(){

        viewModel.newsList.observe(this, Observer {
            viewDataBinding.actMainRv.apply {
                (adapter as NewsRecyclerViewAdapter).run {
                    replaceAll(it)
                    notifyItemRangeChanged(0, it.size)
                }

                (itemAnimator as SimpleItemAnimator).run {
                    changeDuration = 0
                    supportsChangeAnimations = false
                }
            }
        })

        viewDataBinding.actMainSrl.apply{
            setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
                override fun onRefresh() {
                    viewModel.getNewsList()

                    this@apply.isRefreshing = false
                }
            })
        }
    }

    private fun setProgressBar(){
        viewModel.isProgress.observe(this, Observer {
            viewDataBinding.actMainPb.visibility = it
        })

    }

}


