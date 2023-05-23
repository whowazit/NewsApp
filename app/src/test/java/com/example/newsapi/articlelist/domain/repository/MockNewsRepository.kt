package com.example.newsapi.articlelist.domain.repository

import com.example.newsapi.articlelist.domain.model.Article
import com.example.newsapi.articlelist.domain.model.News
import com.example.newsapi.core.utils.Resource

class MockNewsRepository: NewsRepository {

    private val news = News(
        articles = emptyList(),
        status = "",
        totalResults = 0
    )

    private var shouldReturnNetworkError: Boolean = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun getNews(source: String, page: Int): Resource<News> {
        if (shouldReturnNetworkError) {
            return Resource.Error(message = "")
        }
        return Resource.Success(data = news)
    }
}