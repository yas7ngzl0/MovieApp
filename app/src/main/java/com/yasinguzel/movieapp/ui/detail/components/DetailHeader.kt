package com.yasinguzel.movieapp.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yasinguzel.movieapp.data.model.Movie
import com.yasinguzel.movieapp.ui.theme.PrimaryRed
import com.yasinguzel.movieapp.util.Constants

@Composable
fun DetailHeader(
    movie: Movie,
    onPlayClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        //  Backdrop Image (Large background image)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Constants.IMAGE_BASE_URL + (movie.backdropPath ?: movie.posterPath))
                .crossfade(true)
                .build(),
            contentDescription = "Backdrop",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        //  Gradient Shadow (Darken bottom for text/poster visibility)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background),
                        startY = 500f
                    )
                )
        )

        //  Small Poster (Positioned at Bottom-Left)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Constants.IMAGE_BASE_URL + movie.posterPath)
                .crossfade(true)
                .build(),
            contentDescription = "Poster",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 0.dp)
                .width(120.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        //  Play Button (Positioned at Bottom-Right)
        FloatingActionButton(
            onClick = onPlayClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            containerColor = PrimaryRed, // Custom red color
            contentColor = Color.White,  // Always white icon
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}