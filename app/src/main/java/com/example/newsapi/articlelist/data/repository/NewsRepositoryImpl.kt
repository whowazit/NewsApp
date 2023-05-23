package com.example.newsapi.articlelist.data.repository

import com.example.newsapi.core.utils.Resource
import com.example.newsapi.articlelist.data.remote.NewsApi
import com.example.newsapi.articlelist.data.remote.dto.toArticle
import com.example.newsapi.articlelist.data.remote.dto.toNews
import com.example.newsapi.articlelist.domain.model.Article
import com.example.newsapi.articlelist.domain.model.News
import com.example.newsapi.articlelist.domain.repository.NewsRepository
import okio.IOException
import retrofit2.HttpException

class NewsRepositoryImpl(
    private val newsApi: NewsApi
): NewsRepository {

    override suspend fun getNews(
        source: String,
        page: Int,
    ): Resource<News> {
        return try {
            val response = newsApi.getNewsFromSource(source = source, page = page)
            Resource.Success(data = response.toNews())
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred")
        } catch (e: IOException) {
            Resource.Error("Couldn't reach server. Check your internet connection")
        }
    }
}