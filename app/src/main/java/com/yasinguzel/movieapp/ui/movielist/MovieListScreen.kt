package com.yasinguzel.movieapp.ui.movielist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yasinguzel.movieapp.R
import com.yasinguzel.movieapp.ui.components.ErrorScreen
import com.yasinguzel.movieapp.ui.components.MovieItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    categoryTitle: String // We pass the title to display (e.g., "Popular")
) {
    val viewModel: MovieListViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    // Scroll state to detect when we reach the bottom
    val scrollState = rememberLazyGridState()

    // Logic to detect end of list and trigger pagination
    // We use derivedStateOf to avoid unnecessary recompositions
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Load more if we are within 3 items of the end and not already loading
            lastVisibleItemIndex >= (totalItems - 3) && !state.isLoading && !state.endReached
        }
    }

    // Trigger the loadNextPage function when shouldLoadMore becomes true
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadNextPage()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = categoryTitle, // Dynamic title based on category
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                // Infinite Scroll Grid
                LazyVerticalGrid(
                    state = scrollState,
                    columns = GridCells.Fixed(3), // 3 Columns for density
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(state.movies) { movie ->
                        MovieItem(
                            movie = movie,
                            onMovieClick = onMovieClick
                        )
                    }
                }

                // Bottom Loading Indicator (Pagination Loader)
                if (state.isLoading && state.movies.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
            }

            // Initial Loading Indicator (Center of screen)
            if (state.isLoading && state.movies.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // Error Message
            // 3. Error Screen (Only if list is empty - Initial Load Error)
            if (state.error != null && state.movies.isEmpty()) {
                ErrorScreen(
                    message = state.error ?: "Unknown Error",
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}