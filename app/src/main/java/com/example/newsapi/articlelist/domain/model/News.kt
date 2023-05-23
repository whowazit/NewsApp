package com.example.newsapi.articlelist.domain.model

import com.example.newsapi.articlelist.domain.model.Article

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
