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

// UI State holding the search query, results, history, and loading status
data class SearchState(
    val query: String = "",
    val movies: List<Movie> = emptyList(),
    val history: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel for the Search Screen.
 * * REFACTOR FOR TESTING:
 * We switched to Constructor Injection. Instead of creating 'repository' and 'historyManager'
 * inside the class, we pass them as arguments. This allows us to pass Mock (fake) versions
 * of these dependencies when writing Unit Tests.
 */
class SearchViewModel(
    application: Application,
    // CHANGE 1: Injected Repository with default value for production code
    private val repository: MovieRepository = MovieRepository(),
    // CHANGE 2: Injected HistoryManager with default value for production code
    private val historyManager: SearchHistoryManager = SearchHistoryManager(application)
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        // Load search history immediately when ViewModel starts
        loadHistory()
    }

    private fun loadHistory() {
        _state.value = _state.value.copy(history = historyManager.getHistory())
    }

    /**
     * Called when the user types in the search bar.
     * Implements Debounce logic to avoid spamming the API.
     */
    fun onQueryChange(newQuery: String) {
        _state.value = _state.value.copy(query = newQuery)

        // Cancel the previous search job if the user keeps typing
        searchJob?.cancel()

        if (newQuery.isBlank()) {
            // If query is cleared, show history again and clear movie results
            _state.value = _state.value.copy(
                movies = emptyList(),
                history = historyManager.getHistory()
            )
            return
        }

        // Start a new search with a delay (Debounce)
        searchJob = viewModelScope.launch {
            delay(1000L) // Wait for 1 second pause before hitting the API
            performSearch(newQuery)
        }
    }

    fun onHistoryItemClick(historyItem: String) {
        onQueryChange(historyItem) // Set text and trigger search immediately
    }

    fun onDeleteHistoryItem(item: String) {
        historyManager.removeItem(item)
        loadHistory() // Refresh the UI list
    }

    private suspend fun performSearch(query: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)

        // Save this valid search to history
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