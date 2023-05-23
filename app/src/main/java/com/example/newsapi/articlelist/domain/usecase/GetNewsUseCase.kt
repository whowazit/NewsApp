package com.example.newsapi.articlelist.domain.usecase

import com.example.newsapi.articlelist.domain.model.News
import com.example.newsapi.articlelist.domain.repository.NewsRepository
import com.example.newsapi.core.utils.Resource
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend fun execute(
        source: String,
        page: Int,
    ): Resource<News> = repository.getNews(source = source, page = page)
}