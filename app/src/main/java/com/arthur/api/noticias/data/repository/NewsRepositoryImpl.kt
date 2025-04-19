package com.arthur.api.noticias.data.repository

import com.arthur.api.noticias.data.remote.NewsAPI
import com.arthur.api.noticias.domain.model.Article
import com.arthur.api.noticias.domain.repository.NewsRepository
import com.arthur.api.util.Resource

class NewsRepositoryImpl(
    private val newsAPI: NewsAPI

) : NewsRepository {
    override suspend fun getTopHeadlines(category: String): Resource<List<Article>> {


        return try {
            val response = newsAPI.getBreakingNews(category = category)
            Resource.Success(response.articles)
        } catch (e: Exception) {

            Resource.Error(message = "Failed to fetch news ${e.message}")
        } as Resource<List<Article>>


    }

    override suspend fun searchForNews(query: String): Resource<List<Article>> {
       return try {
            val response = newsAPI.searchForNews(query = query)
            Resource.Success(response.articles)
        } catch (e: Exception) {
            Resource.Error(message = "Failed to fetch news ${e.message}")
        }


    }

}







