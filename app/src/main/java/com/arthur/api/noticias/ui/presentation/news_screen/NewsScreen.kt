package com.arthur.api.noticias.ui.presentation.news_screen

import android.widget.Button
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.room.util.TableInfo
import com.arthur.api.noticias.domain.model.Article
import com.arthur.api.noticias.ui.presentation.component.BottoSheetContent
import com.arthur.api.noticias.ui.presentation.component.CategoryTabRow
import com.arthur.api.noticias.ui.presentation.component.NewsArticleCard
import com.arthur.api.noticias.ui.presentation.component.NewsScreenTopBar
import com.arthur.api.noticias.ui.presentation.component.RetryContent
import com.arthur.api.noticias.ui.presentation.component.SearchAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun NewsScreen(
    state: NewsScreenState,
    onEvent: (NewsScreenEvent) -> Unit,
    onReadFullStoryClicked: (String) -> Unit

) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    val coroutineScope = rememberCoroutineScope()
    val categories =
        listOf("General", "Technology", "Business", "Health", "Science", "Sports", "Entertainment")
    val pageState = rememberPagerState(initialPage = 0) { categories.size }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var shouldBottomSheetShow by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if (shouldBottomSheetShow) {
        ModalBottomSheet(
            onDismissRequest = { shouldBottomSheetShow = false },
            sheetState = sheetState,
            content = {
                state.selectedArticle?.let {
                    BottoSheetContent(
                        article = it,
                        onReadFullStoryClicked = {
                            onReadFullStoryClicked(it.url)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                                .invokeOnCompletion {
                                    shouldBottomSheetShow = false


                                }

                        }
                    )

                }


            }

        )


    }

    LaunchedEffect(key1 = pageState) {
        snapshotFlow { pageState.currentPage }.collect { page ->
            onEvent(NewsScreenEvent.OnCategoryChanged(categories[page]))
        }


    }

    LaunchedEffect(key1 = Unit) {
        if (state.searchQuery.isNotEmpty()) {
            onEvent(NewsScreenEvent.OnSearchQueryChanged(searchQuery = state.searchQuery))
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Crossfade(targetState = state.isSearchBarVisible) { isVisible ->
            if (isVisible) {
                Column {

                    SearchAppBar(


                        modifier = Modifier.focusRequester(focusRequester),
                        value = state.searchQuery,
                        onInputValueChange = { newValue ->
                            onEvent(NewsScreenEvent.OnSearchQueryChanged(newValue))
                        },
                        onCloseIconClicked = { onEvent(NewsScreenEvent.OnCloseIconClicked) },
                        onSearchClicked = {
                            keyboardController?.hide()
                            focusManager.clearFocus()




                        })

                    NewsArticlesList(
                        state = state,
                        //bottom sheet
                        onCardClicked = { article ->
                            shouldBottomSheetShow = true
                            onEvent(NewsScreenEvent.OnNewsCardClicked(article = article))

                        },
                        onRetry = {
                            onEvent(NewsScreenEvent.OnCategoryChanged(state.searchQuery))
                        }

                    )
                }

            } else {

                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        NewsScreenTopBar(

                            scrollBehavior = scrollBehavior,
                            onSearchIconClicked = {
                                coroutineScope.launch {
                                    delay(500)
                                                focusRequester.requestFocus()
                                }
                                onEvent(NewsScreenEvent.OnSearchIconClicked)


                            })
                    }
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        CategoryTabRow(
                            pageState = pageState,
                            categories = categories,
                            onTabSelected = { index ->

                                coroutineScope.launch {
                                    pageState.animateScrollToPage(index)
                                }
                            }
                        )
                        HorizontalPager(

                            modifier = Modifier.fillMaxSize(),
                            state = pageState,

                            ) {

                            NewsArticlesList(
                                state = state,
                                //bottom sheet
                                onCardClicked = { article ->
                                    shouldBottomSheetShow = true
                                    onEvent(NewsScreenEvent.OnNewsCardClicked(article = article))

                                },
                                onRetry = {
                                    onEvent(NewsScreenEvent.OnCategoryChanged(state.category))
                                }

                            )

                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(16.dp),

                            ) {
                            items(state.articles) { article ->
                                NewsArticleCard(
                                    article = article,
                                    onCardClicked = { }
                                )
                            }
                        }


                    }


                }

            }


        }

    }


}

//////
@Composable
fun NewsArticlesList(
    state: NewsScreenState,
    onCardClicked: (Article) -> Unit,
    onRetry: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(state.articles) { article ->
            NewsArticleCard(
                article = article,
                onCardClicked = onCardClicked

            )


        }

    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        if (state.error != null) {
            RetryContent(
                error = state.error,
                onRetry = onRetry
            )

        }

    }

}