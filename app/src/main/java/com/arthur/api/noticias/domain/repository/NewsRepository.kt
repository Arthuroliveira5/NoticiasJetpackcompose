package com.arthur.api.noticias.domain.repository

import com.arthur.api.noticias.domain.model.Article
import com.arthur.api.util.Resource

interface
NewsRepository {


    suspend fun getTopHeadlines(

        category: String,
    ): Resource<List<Article>>


    suspend fun searchForNews(
        query: String
    ): Resource<List<Article>>

}




  

