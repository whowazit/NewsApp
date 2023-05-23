package com.example.newsapi.articlelist.domain.repository

import com.example.newsapi.articlelist.domain.model.News
import com.example.newsapi.core.utils.Resource

interface NewsRepository {
    suspend fun getNews(
        source: String,
        page: Int,
    ): Resource<News>
}