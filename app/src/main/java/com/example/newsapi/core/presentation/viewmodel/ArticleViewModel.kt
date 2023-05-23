package com.example.newsapi.core.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapi.BuildConfig
import com.example.newsapi.articlelist.domain.model.Article
import com.example.newsapi.articlelist.domain.usecase.GetNewsUseCase
import com.example.newsapi.articlelist.presentation.state.ArticleListState
import com.example.newsapi.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val newsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _articleState = MutableSharedFlow<ArticleListState>()
    val articleState: SharedFlow<ArticleListState> = _articleState

    var allArticles = emptyList<Article>()

    private var selectedArticle: Article? = null
    private var currentPage = 1
    private var totalResult = 0

    init {
        getNewsBySource(source = BuildConfig.SOURCE)
    }

    fun getNewsBySource(source: String) {
        viewModelScope.launch {
            val result = newsUseCase.execute(source = source, page = currentPage++)

            when(result) {
                is Resource.Success -> {
                    totalResult = result.data?.totalResults ?: 0
                    _articleState.emit(ArticleListState(articles = result.data?.articles ?: emptyList()))
                }
                is Resource.Error -> {
                    _articleState.emit(ArticleListState(error = result.message ?: ""))
                }
            }
        }
    }

    fun setSelectedArticle(article: Article) {
        selectedArticle = article
    }

    fun getSelectedArticle(): Article? {
        return selectedArticle
    }

    fun hasMoreData(size: Int): Boolean {
        return size < totalResult
    }
}