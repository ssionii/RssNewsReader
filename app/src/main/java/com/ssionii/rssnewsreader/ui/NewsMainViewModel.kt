package com.ssionii.rssnewsreader.ui

import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ssionii.rssnewsreader.MainActivity
import com.ssionii.rssnewsreader.data.News
import com.ssionii.rssnewsreader.util.GetXMLTask

class NewsMainViewModel(val activity: MainActivity) : ViewModel() {

    private var _newsList = MutableLiveData<MutableList<News>>()
    val newsList: LiveData<MutableList<News>> get() = _newsList

    private var _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    init {
        getNewsList()
    }

    fun getNewsList() {
        showProgress()

        val xmlTask = GetXMLTask()
        xmlTask.execute()

        xmlTask.isGetDataDone.observe(activity, Observer {
            if (it) {
                hideProgress()
                val sortedList = xmlTask.newsList.sortedWith(compareBy({ it.newsIdx }))
                _newsList.postValue(sortedList.toMutableList())
            }

        })
    }

    private fun showProgress() {
        _isProgress.value = VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = INVISIBLE
    }
}