package com.example.newsapi.articlelist.data.remote.dto

import com.example.newsapi.articlelist.domain.model.News

data class NewsDto(
    val articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
)

fun NewsDto.toNews(): News {
    return News(
        articles = articles.map {
            it.toArticle()
        },
        status = status,
        totalResults = totalResults
    )
}