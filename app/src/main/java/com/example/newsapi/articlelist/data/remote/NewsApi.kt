package com.example.newsapi.articlelist.data.remote

import com.example.newsapi.core.utils.Constants.API_KEY
import com.example.newsapi.articlelist.data.remote.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines/")
    suspend fun getNewsFromSource(
        @Query("sources") source: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 6,
        @Query("apiKey") apiKey: String = API_KEY,
    ): NewsDto
}