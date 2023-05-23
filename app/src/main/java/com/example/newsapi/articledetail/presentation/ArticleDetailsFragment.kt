package com.example.newsapi.articledetail.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.newsapi.core.presentation.viewmodel.ArticleViewModel
import com.example.newsapi.databinding.FragmentArticleDetailBinding

class ArticleDetailsFragment: Fragment() {

    private lateinit var binding: FragmentArticleDetailBinding

    private val viewModel: ArticleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        with(binding) {
            viewModel.getSelectedArticle()?.let { article ->
                articleDetailsTitleTv.text = article.title
                articleDetailsDescTv.text = article.description
                articleDetailsContentTv.text = article.content
                Glide.with(root).load(article.urlToImage).into(articleDetailsIv)
            }
        }
    }
}