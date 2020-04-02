package com.ssionii.rssnewsreader.data

import java.io.Serializable

data class News(
    var newsIdx: Int,
    var title: String,
    var thumbnail: String?,
    var description: String?,
    var url: String,
    var keyword1: String,
    var keyword2: String,
    var keyword3: String
) : Serializable