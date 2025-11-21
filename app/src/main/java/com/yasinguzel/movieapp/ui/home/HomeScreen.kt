package com.yasinguzel.movieapp.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yasinguzel.movieapp.R
import com.yasinguzel.movieapp.ui.components.MovieSection

@Composable
fun HomeScreen(
    // Navigation callback: When a movie is clicked, trigger this function
    onMovieClick: (Int) -> Unit = {}
) {
    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            // Simple Title for the screen
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Loading Indicator
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // 2. Error Message
            state.error?.let { errorMsg ->
                Text(
                    text = stringResource(R.string.error_occurred, errorMsg),
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // 3. Main Content (Scrollable List of Sections)
            if (!state.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // Enable vertical scrolling
                        .padding(bottom = 16.dp)
                ) {
                    // Now Playing Section
                    MovieSection(
                        title = stringResource(R.string.now_playing),
                        movies = state.nowPlayingMovies,
                        onMovieClick = onMovieClick
                        // onSeeAllClick = { /* Navigate to List Screen */ } // İleride burayı dolduracağız
                    )

                    // Popular Section
                    MovieSection(
                        title = stringResource(R.string.popular),
                        movies = state.popularMovies,
                        onMovieClick = onMovieClick
                    )

                    // Top Rated Section
                    MovieSection(
                        title = stringResource(R.string.top_rated),
                        movies = state.topRatedMovies,
                        onMovieClick = onMovieClick
                    )

                    // Upcoming Section
                    MovieSection(
                        title = stringResource(R.string.upcoming),
                        movies = state.upcomingMovies,
                        onMovieClick = onMovieClick
                    )
                }
            }
        }
    }
}