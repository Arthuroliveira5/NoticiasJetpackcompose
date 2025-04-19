package com.arthur.api.noticias.data.remote

import com.arthur.api.noticias.domain.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    //ENDPOINTS
    @GET("top-headlines")
    suspend fun getBreakingNews(
        @Query("category") category: String,
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

    //search for news ENDPOINT
    @GET("everything")
    suspend fun searchForNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

    companion object {
        const val API_KEY = "2b3350c2e130493a94f280d8c05ca388"
        const val BASE_URL = "https://newsapi.org/v2/"
    }


}