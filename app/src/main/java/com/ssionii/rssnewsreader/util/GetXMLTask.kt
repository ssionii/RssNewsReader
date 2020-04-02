package com.ssionii.rssnewsreader.util

import android.os.AsyncTask
import android.text.Html
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssionii.rssnewsreader.R
import com.ssionii.rssnewsreader.data.News
import org.jsoup.Jsoup
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import java.util.Map
import kotlin.Comparator


class GetXMLTask : AsyncTask<String, Void, Document>(){

    var doc : Document? = null
    val rssUrl = "https://news.google.com/rss?hl=ko&gl=KR&ceid=KR:ko"

    lateinit var nodeList: NodeList

    var newsList = arrayListOf<News>()
    private var _isGetDataDone = MutableLiveData<Boolean>()
    val isGetDataDone : LiveData<Boolean> get() = _isGetDataDone


    // xml 통신
    override fun doInBackground(vararg params: String?): Document? {
        val url = URL(rssUrl)
        try {
            val dbf : DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val db = dbf.newDocumentBuilder()

            doc = db.parse(InputSource(url.openStream()))
            doc?.documentElement?.normalize()
        }catch (e: Exception){
            e.printStackTrace()
        }

        return doc
    }

    override fun onPostExecute(result: Document?) {

        doc?.apply {
            nodeList = this.getElementsByTagName("item")

            for (i in 0 until nodeList.length) {
                val node = nodeList.item(i)
                val fstElement: Element = (node as Element)

                val title = fstElement.getElementsByTagName("title")
                val link = fstElement.getElementsByTagName("link")

                HtmlParser().getNewsData(i, title.item(0).childNodes.item(0).nodeValue
                    ,link.item(0).childNodes.item(0).nodeValue)

            }
        }

        super.onPostExecute(result)
    }


    inner class HtmlParser {

        fun getNewsData(idx: Int, title: String, urlToRead : String) {

            val thread = Thread(Runnable {
                try{
                    val ssl = SSLConnect()
                    ssl.postHttps(urlToRead, 2000, 2000)

                    val doc = Jsoup.connect(urlToRead).get()
                    val imgUrl = doc.select("meta[property=og:image]").attr("content")
                    val description = doc.select("meta[property=og:description]").attr("content")

                    // description에서 keyword 뽑아내기
                    val keywords = getKeyword(description)

                    if(description.isNotEmpty()){
                        newsList.add( News(idx, title, imgUrl, description, urlToRead, keywords[0], keywords[1], keywords[2]))
                    }else{
                        newsList.add( News(idx, title, imgUrl, description, urlToRead, "", "", ""))
                    }

                    if(newsList.size == nodeList.length - 1)
                        _isGetDataDone.postValue(true)

                }catch (e: Exception){
                    e.printStackTrace()
                }
            })

            thread.start()

        }


        fun getKeyword(description: String) : List<String>{

            val keywords = arrayListOf<String>()
            val map = HashMap<String, Int>()

            val stringTokenizer = StringTokenizer(description, " |,|.")
            var s = ""
            while(stringTokenizer.hasMoreTokens()){
                s = stringTokenizer.nextToken()

                if(map.containsKey(s)){
                    var cnt = map[s]
                    map.put(s, cnt!!.plus(1) )
                }else{
                    map.put(s, 1)
                }
            }

            val sortedMap = map.toSortedMap()
            val resultMap = sortedMap.toList().sortedByDescending { (_, value) -> value }.toMap()

            val itr = resultMap.iterator()
            while(itr.hasNext()) {
                keywords.add(itr.next().key)
                if(keywords.size == 3){
                    break
                }
            }

            return keywords
        }
    }

}
