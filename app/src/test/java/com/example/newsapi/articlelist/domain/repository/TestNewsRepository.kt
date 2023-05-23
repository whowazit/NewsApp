package com.example.newsapi.articlelist.domain.repository

import com.example.newsapi.core.utils.Resource
import com.example.newsapi.core.utils.toStringDateAndTime
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class TestNewsRepository {

    private val repository = MockNewsRepository()

    @Test
    fun testSuccess() {
        runBlocking {
            val response = repository.getNews("", 1)
            assertEquals(true, response is Resource.Success)
        }
    }

    @Test
    fun testError() {
        runBlocking {
            repository.setShouldReturnNetworkError(true)
            val response = repository.getNews("", 1)
            assertEquals(true, response is Resource.Error)
        }
    }
}