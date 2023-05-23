package com.example.newsapi.articlelist.domain.model

import com.example.newsapi.core.data.remote.model.Source

data class Article(
    val author: String,
    val content: String?,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)