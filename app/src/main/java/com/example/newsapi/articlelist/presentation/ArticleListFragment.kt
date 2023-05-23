package com.example.newsapi.articlelist.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.BuildConfig
import com.example.newsapi.R
import com.example.newsapi.articledetail.presentation.ArticleDetailsFragment
import com.example.newsapi.articlelist.presentation.adapters.ArticleAdapter
import com.example.newsapi.core.presentation.viewmodel.ArticleViewModel
import com.example.newsapi.databinding.FragmentArticleListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleListFragment: Fragment() {

    private lateinit var binding: FragmentArticleListBinding
    private lateinit var articleAdapter: ArticleAdapter

    private val viewModel: ArticleViewModel by activityViewModels()

    private var isLoadingMore = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setListeners()
        setStateListeners()
    }

    private fun initViews() {
        with(binding) {
            articleAdapter = ArticleAdapter()
            articleRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = articleAdapter
            }

            if (viewModel.allArticles.isNotEmpty()) {
                articleAdapter.differ.submitList(viewModel.allArticles)
            }
        }
    }

    private fun setListeners() {
        articleAdapter.setOnItemClickListener { article ->
            viewModel.setSelectedArticle(article = article)

            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container, ArticleDetailsFragment(), FRAGMENT_ARTICLE_LIST_TAG)
                .addToBackStack("")
                .commit()
        }

        binding.articleRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!isLoadingMore
                    && !recyclerView.canScrollVertically(1)
                    && viewModel.hasMoreData(size = articleAdapter.itemCount)
                ) {
                    isLoadingMore = true
                    viewModel.getNewsBySource(source = BuildConfig.SOURCE)
                }
            }
        })
    }

    private fun setStateListeners() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.articleState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .distinctUntilChanged()
                .collect {
                if (it.error.isNotEmpty()) {
                    // show error
                } else {
                    if (it.articles.isNotEmpty()) {
                        val currentList = articleAdapter.differ.currentList.toMutableList()
                        currentList.addAll(it.articles)
                        articleAdapter.differ.submitList(currentList)
                        viewModel.allArticles = currentList
                    }
                }
            }
        }
    }

    companion object {
        const val FRAGMENT_ARTICLE_LIST_TAG = "article_list"
    }
}