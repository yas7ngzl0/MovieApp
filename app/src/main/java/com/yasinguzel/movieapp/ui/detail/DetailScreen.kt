package com.yasinguzel.movieapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yasinguzel.movieapp.ui.detail.components.DetailHeader
import com.yasinguzel.movieapp.ui.detail.components.DetailInfo
import androidx.compose.runtime.setValue

@Composable
fun DetailScreen(
    onBackClick: () -> Unit
) {
    val viewModel: DetailViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    var lastClickTime by remember { mutableLongStateOf(0L) }

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. Loading State Indicator
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        // 2. Error State Message
        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 3. Main Content (Displayed when data is loaded)
        state.movie?.let { movie ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState) // Enable vertical scrolling for the whole screen
            ) {
                // Component 1: Header (Image, Play Button)
                DetailHeader(
                    movie = movie,
                    onPlayClick = { /* Handle Play Action */ }
                )

                // Component 2: Info (Title, Overview, Rating)
                DetailInfo(movie = movie)
            }
        }

        // 4. Back Button (Floating on Top-Left)
        IconButton(
            onClick = {
                val currentTime = System.currentTimeMillis()
                // Only allow click if 500ms has passed since the last click
                // to block spam
                if (currentTime - lastClickTime > 1000) {
                    lastClickTime = currentTime
                    onBackClick()
                }
            },
            modifier = Modifier
                // FIX 1: Push down below the status bar (avoid overlap with system clock)
                .statusBarsPadding()
                .padding(16.dp)
                .align(Alignment.TopStart)
                .background(Color.Black.copy(alpha = 0.4f), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}