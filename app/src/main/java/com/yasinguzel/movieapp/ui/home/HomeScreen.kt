package com.yasinguzel.movieapp.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yasinguzel.movieapp.R
import com.yasinguzel.movieapp.ui.components.ErrorScreen
import com.yasinguzel.movieapp.ui.components.MovieSection
import com.yasinguzel.movieapp.ui.navigation.MovieCategory
import com.yasinguzel.movieapp.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // Navigation callback: When a movie is clicked, trigger this function
    onMovieClick: (Int) -> Unit = {},
    // Navigation callback: When 'See All' is clicked (We will use this later)
    onShowAllClick: (MovieCategory) -> Unit = {}
) {
    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            // Center-aligned app bar with profile icon
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Profile Image Button (Right side)
                    IconButton(onClick = { /* TODO: Navigate to Profile */ }) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(Constants.DUMMY_AVATAR)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp) // Size of the icon
                                .clip(CircleShape) // Make it circular
                                .border(
                                    width = 1.5.dp, // Thickness of the frame
                                    color = MaterialTheme.colorScheme.primary, // Theme color (Red)
                                    shape = CircleShape // Circular frame
                                )
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
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
            // 2. Error Message (GÜNCELLENDİ)
            state.error?.let { errorMsg ->
                // Eski basit Text yerine, yeni havalı ekranı kullanıyoruz
                ErrorScreen(
                    message = errorMsg,
                    onRetry = {
                        // Butona basılınca ViewModel'e "Tekrar dene" diyoruz
                        viewModel.retry()
                    }
                )
            }

            // 3. Main Content (Scrollable List of Sections)
            if (!state.isLoading && state.error == null) {
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
                        onMovieClick = onMovieClick,
                        onShowAllClick = { onShowAllClick(MovieCategory.NOW_PLAYING) }
                    )

                    // Popular Section
                    MovieSection(
                        title = stringResource(R.string.popular),
                        movies = state.popularMovies,
                        onMovieClick = onMovieClick,
                        onShowAllClick = { onShowAllClick(MovieCategory.POPULAR) }
                    )

                    // Top Rated Section
                    MovieSection(
                        title = stringResource(R.string.top_rated),
                        movies = state.topRatedMovies,
                        onMovieClick = onMovieClick,
                        onShowAllClick = { onShowAllClick(MovieCategory.TOP_RATED) }
                    )

                    // Upcoming Section
                    MovieSection(
                        title = stringResource(R.string.upcoming),
                        movies = state.upcomingMovies,
                        onMovieClick = onMovieClick,
                        onShowAllClick = { onShowAllClick(MovieCategory.UPCOMING) }
                    )
                }
            }
        }
    }
}