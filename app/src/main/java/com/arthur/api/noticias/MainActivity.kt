package com.arthur.api.noticias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.arthur.api.noticias.ui.presentation.news_screen.NewsScreen
import com.arthur.api.noticias.ui.presentation.news_screen.NewsScreenViewModel
import com.arthur.api.noticias.ui.theme.NoticiasTheme
import com.arthur.api.util.NavGraphSetup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoticiasTheme {
                val navController = rememberNavController()
                NavGraphSetup(navController = navController)

                val viewModel: NewsScreenViewModel = hiltViewModel()



            }
        }
    }
}



