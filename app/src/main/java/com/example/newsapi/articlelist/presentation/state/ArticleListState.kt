package com.example.newsapi.articlelist.presentation.state

import com.example.newsapi.articlelist.domain.model.Article

data class ArticleListState(
    val articles: List<Article> = emptyList(),
    val error: String = "",
)