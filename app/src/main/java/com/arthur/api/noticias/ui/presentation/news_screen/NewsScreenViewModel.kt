package com.arthur.api.noticias.ui.presentation.news_screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arthur.api.noticias.domain.model.Article
import com.arthur.api.noticias.domain.repository.NewsRepository
import com.arthur.api.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsScreenViewModel @Inject constructor(
    private val newsRepository: NewsRepository,


    ) : ViewModel() {



    var state by mutableStateOf(NewsScreenState())
    private var searchJob: Job? = null


    fun onEvent(event: NewsScreenEvent) {
        when (event) {
            is NewsScreenEvent.OnCategoryChanged -> {
                state = state.copy(category = event.category)
                getNewsArticles(category = event.category)

            }

            NewsScreenEvent.OnCloseIconClicked ->
            {
                state = state.copy(isSearchBarVisible = false)
                getNewsArticles(category = state.category)

            }
            is NewsScreenEvent.OnNewsCardClicked -> {
                state = state.copy(selectedArticle = event.article)


            }
            NewsScreenEvent.OnSearchIconClicked -> {
                state = state.copy(isSearchBarVisible = true,articles = emptyList())


            }
            is NewsScreenEvent.OnSearchQueryChanged ->

            {
                state = state.copy(searchQuery = event.searchQuery)

                searchJob?.cancel()
                searchJob = viewModelScope.launch {

                   delay(1000L)
                    searchForNews(query = state.searchQuery)

                }
                searchForNews(query = state.searchQuery)


            }
        }
    }

    init {
        getNewsArticles(category = "general")
    }


    private fun getNewsArticles(category: String) {

        state = state.copy(isLoading = true)
        viewModelScope.launch {
            val result = newsRepository.getTopHeadlines(category = category)
            when (result) {
                is Resource.Success -> {

                    state = state.copy(
                        articles = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )

                }

                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        articles = emptyList()
                    )


                }

            }


        }


    }


    private fun searchForNews(query: String) {

        if(query.isEmpty())

        { return



        }

        viewModelScope.launch {



        }

        state = state.copy(isLoading = true)
        viewModelScope.launch {
            val result = newsRepository.searchForNews(query = query)
            when (result) {
                is Resource.Success -> {

                    state = state.copy(
                        articles = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )

                }

                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        articles = emptyList()
                    )


                }

            }


        }


    }

}