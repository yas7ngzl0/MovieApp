package com.yasinguzel.movieapp.ui.movielist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinguzel.movieapp.data.model.Movie
import com.yasinguzel.movieapp.data.repository.MovieRepository
import com.yasinguzel.movieapp.ui.navigation.MovieCategory
import com.yasinguzel.movieapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State to manage the Infinite List UI
data class MovieListState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(), // The growing list of movies
    val error: String? = null,
    val endReached: Boolean = false, // True if no more pages are available
    val page: Int = 1 // Current page number
)

class MovieListViewModel(
    private val savedStateHandle: SavedStateHandle // To read navigation arguments
) : ViewModel() {

    private val repository = MovieRepository()

    private val _state = MutableStateFlow(MovieListState())
    val state: StateFlow<MovieListState> = _state.asStateFlow()

    // Determine which category to load based on the navigation argument
    private val category: String = savedStateHandle.get<String>("category") ?: MovieCategory.POPULAR.key

    init {
        loadNextPage()
    }

    /**
     * Retries loading the data.
     * If it's the first page or a next page failure, calling loadNextPage() handles it.
     */
    fun retry() {
        loadNextPage()
    }

    fun loadNextPage() {
        // Prevent duplicate requests if already loading or if we reached the end
        if (_state.value.isLoading || _state.value.endReached) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            // Select the correct API call based on the category key
            val result = when (category) {
                MovieCategory.NOW_PLAYING.key -> repository.getNowPlayingMovies(_state.value.page)
                MovieCategory.POPULAR.key -> repository.getPopularMovies(_state.value.page)
                MovieCategory.TOP_RATED.key -> repository.getTopRatedMovies(_state.value.page)
                MovieCategory.UPCOMING.key -> repository.getUpcomingMovies(_state.value.page)
                else -> repository.getPopularMovies(_state.value.page)
            }

            when (result) {
                is Resource.Success -> {
                    val newMovies = result.data?.results ?: emptyList()

                    _state.value = _state.value.copy(
                        isLoading = false,
                        endReached = newMovies.isEmpty(), // If list is empty, we are done
                        page = _state.value.page + 1, // Increment page for next call
                        movies = _state.value.movies + newMovies // Append new items to existing list
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> Unit
            }
        }
    }
}