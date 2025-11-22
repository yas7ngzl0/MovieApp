package com.yasinguzel.movieapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinguzel.movieapp.data.model.Movie
import com.yasinguzel.movieapp.data.repository.MovieRepository
import com.yasinguzel.movieapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// UI State holding the data for the Detail Screen
data class DetailState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val error: String? = null
)

class DetailViewModel(
    savedStateHandle: SavedStateHandle // Automatically intercepts arguments passed from Navigation
) : ViewModel() {

    private val repository = MovieRepository()
    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    // Store the movie ID to allow retrying the request if it fails
    private val movieId: Int? = savedStateHandle.get<String>("movieId")?.toIntOrNull()

    init {
        // Check if the ID exists and fetch details initially
        if (movieId != null) {
            getMovieDetail(movieId)
        }
    }

    /**
     * Retries the movie detail fetch request.
     * This function is called when the user clicks "Retry" on the ErrorScreen.
     */
    fun retry() {
        if (movieId != null) {
            getMovieDetail(movieId)
        }
    }

    private fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            // Reset error state and set loading to true before making the request
            _state.value = _state.value.copy(isLoading = true, error = null)

            when(val result = repository.getMovieDetail(id)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        movie = result.data,
                        error = null
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