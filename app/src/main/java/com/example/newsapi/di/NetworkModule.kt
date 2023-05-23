package com.example.newsapi.di

import com.example.newsapi.BuildConfig
import com.example.newsapi.core.utils.Constants.BASE_URL
import com.example.newsapi.articlelist.data.remote.NewsApi
import com.example.newsapi.articlelist.data.repository.NewsRepositoryImpl
import com.example.newsapi.articlelist.domain.repository.NewsRepository
import com.example.newsapi.articlelist.domain.usecase.GetNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().also {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(loggingInterceptor)
            }
        }.build()

    @Singleton
    @Provides
    fun provideRetrofitBuilder(client: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)

    @Singleton
    @Provides
    fun provideAirportService(retrofit: Retrofit.Builder): NewsApi =
        retrofit.build().create(NewsApi::class.java)

    @Singleton
    @Provides
    fun provideNewsRepository(newsApi: NewsApi): NewsRepository =
        NewsRepositoryImpl(newsApi = newsApi)

    @Singleton
    @Provides
    fun provideNewsUseCase(repository: NewsRepository): GetNewsUseCase =
        GetNewsUseCase(repository = repository)
}