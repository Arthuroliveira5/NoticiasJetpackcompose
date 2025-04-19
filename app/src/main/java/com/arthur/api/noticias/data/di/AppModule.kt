package com.arthur.api.noticias.data.di

import com.arthur.api.noticias.data.remote.NewsAPI
import com.arthur.api.noticias.data.repository.NewsRepositoryImpl
import com.arthur.api.noticias.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(NewsAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        return retrofit.create(NewsAPI::class.java)


    }


    @Provides
    @Singleton
    fun provideNewsRepository(newsAPI: NewsAPI): NewsRepository {
        return NewsRepositoryImpl(newsAPI)
    }

}