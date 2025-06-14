package com.arthur.api.noticias.ui.presentation.component

import android.R.attr.text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryTabRow(pageState: PagerState,
                   categories: List<String>,
                   onTabSelected: (Int) -> Unit,

                   ) {
    ScrollableTabRow(
        selectedTabIndex = pageState.currentPage,
        edgePadding = 0.dp,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = pageState.currentPage == index,
                onClick = { onTabSelected(index) },
               content = {
                   Text(
                       text=category,
                       modifier = Modifier.padding(vertical=8.dp,horizontal=2.dp)
                   )



               },
            )
        }




    }

}