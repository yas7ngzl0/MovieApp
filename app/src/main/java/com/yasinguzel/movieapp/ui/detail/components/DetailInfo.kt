package com.yasinguzel.movieapp.ui.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yasinguzel.movieapp.R
import com.yasinguzel.movieapp.data.model.Movie
import com.yasinguzel.movieapp.ui.theme.RatingStarColor

@Composable
fun DetailInfo(movie: Movie) {
    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(16.dp))

        // Movie Title
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Rating and Release Date Row
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Star Icon and Rating Score
            Text(
                text = stringResource(id = R.string.rating_format, movie.voteAverage),
                color = RatingStarColor, // Custom Gold Color
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Release Date
            Text(
                text = movie.releaseDate ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        // Overview Section Title
        Text(
            text = stringResource(R.string.overview),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        // Overview Body Text
        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )

        // Bottom padding for better scroll experience
        Spacer(modifier = Modifier.height(50.dp))
    }
}