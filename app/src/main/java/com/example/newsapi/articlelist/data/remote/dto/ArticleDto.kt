package com.example.newsapi.articlelist.data.remote.dto

import com.example.newsapi.core.data.remote.model.Source
import com.example.newsapi.articlelist.domain.model.Article
import com.example.newsapi.core.utils.toStringDateAndTime

data class ArticleDto(
    val author: String,
    val content: String?,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)

fun ArticleDto.toArticle(): Article {
    return Article(
        author,
        content,
        description,
        publishedAt.toStringDateAndTime("dd MMM, yyyy hh:mma"),
        source,
        title,
        url,
        urlToImage
    )
}