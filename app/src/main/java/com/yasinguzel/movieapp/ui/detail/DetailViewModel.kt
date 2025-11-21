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

// UI State for Detail Screen
data class DetailState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val error: String? = null
)

class DetailViewModel(
    savedStateHandle: SavedStateHandle // Automatically intercepts arguments from Navigation
) : ViewModel() {

    private val repository = MovieRepository()
    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    init {
        // Retrieve the "movieId" argument defined in NavGraph
        val movieId = savedStateHandle.get<String>("movieId")?.toIntOrNull()
        if (movieId != null) {
            getMovieDetail(movieId)
        }
    }

    private fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

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