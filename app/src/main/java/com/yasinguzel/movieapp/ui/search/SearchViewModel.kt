package com.yasinguzel.movieapp.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yasinguzel.movieapp.data.model.Movie
import com.yasinguzel.movieapp.data.repository.MovieRepository
import com.yasinguzel.movieapp.util.Resource
import com.yasinguzel.movieapp.util.SearchHistoryManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Updated State with History list
data class SearchState(
    val query: String = "",
    val movies: List<Movie> = emptyList(),
    val history: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Changed to AndroidViewModel to access Application Context
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository()
    private val historyManager = SearchHistoryManager(application) // Initialize Manager

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        // Load history when ViewModel starts
        loadHistory()
    }

    private fun loadHistory() {
        _state.value = _state.value.copy(history = historyManager.getHistory())
    }

    fun onQueryChange(newQuery: String) {
        _state.value = _state.value.copy(query = newQuery)
        searchJob?.cancel()

        if (newQuery.isBlank()) {
            // If query is empty, show history again and clear movies
            _state.value = _state.value.copy(
                movies = emptyList(),
                history = historyManager.getHistory()
            )
            return
        }

        searchJob = viewModelScope.launch {
            delay(1000L)
            performSearch(newQuery)
        }
    }

    fun onHistoryItemClick(historyItem: String) {
        onQueryChange(historyItem) // Set text and trigger search immediately
    }

    fun onDeleteHistoryItem(item: String) {
        historyManager.removeItem(item)
        loadHistory() // Refresh UI
    }

    private suspend fun performSearch(query: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)

        // Save valid search to history
        historyManager.addSearchQuery(query)

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