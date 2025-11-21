package com.yasinguzel.movieapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinguzel.movieapp.data.model.Movie
import com.yasinguzel.movieapp.data.repository.MovieRepository
import com.yasinguzel.movieapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// UI State for Search Screen
data class SearchState(
    val query: String = "",
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SearchViewModel : ViewModel() {

    private val repository = MovieRepository()
    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    // Called whenever the user types a character
    fun onQueryChange(newQuery: String) {
        _state.value = _state.value.copy(query = newQuery)

        // Cancel the previous job (debounce mechanism)
        searchJob?.cancel()

        // If query is empty, clear results and stop
        if (newQuery.isBlank()) {
            _state.value = _state.value.copy(movies = emptyList())
            return
        }

        // Start a new search job with a 500ms delay to prevent API spamming
        searchJob = viewModelScope.launch {
            delay(500L)
            performSearch(newQuery)
        }
    }

    private suspend fun performSearch(query: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)

        when (val result = repository.searchMovies(query = query, page = 1)) {
            is Resource.Success -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    movies = result.data?.results ?: emptyList()
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