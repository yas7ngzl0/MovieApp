package com.yasinguzel.movieapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yasinguzel.movieapp.R
import com.yasinguzel.movieapp.data.model.Movie

/**
 * Reusable Composable for a movie category section.
 * Displays a title with a 'See All' button and a horizontal list of movies.
 */
@Composable
fun MovieSection(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onShowAllClick: () -> Unit = {} // Yeni: Tümünü gör tıklama olayı (Şimdilik boş varsayılan)
) {
    if (movies.isNotEmpty()) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {

            // --- Header Row (Title + See All Button) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // İki uca yasla
                verticalAlignment = Alignment.CenterVertically // Dikeyde ortala
            ) {
                // Section Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                // See All Button
                TextButton(onClick = onShowAllClick) {
                    Text(
                        text = stringResource(id = R.string.show_all),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary // Temanın ana rengini kullan
                    )
                }
            }

            // --- Horizontal List ---
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movies) { movie ->
                    MovieItem(movie = movie, onMovieClick = onMovieClick)
                }
            }
        }
    }
}