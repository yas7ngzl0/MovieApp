package com.yasinguzel.movieapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinguzel.movieapp.data.model.Movie
import com.yasinguzel.movieapp.data.repository.MovieRepository
import com.yasinguzel.movieapp.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State holder for the Home Screen UI
data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val nowPlayingMovies: List<Movie> = emptyList(),
    val popularMovies: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList()
)

// REFACTOR: Constructor Injection for Testability.
// We pass the repository as a parameter to allow mocking in Unit Tests.
// A default value "= MovieRepository()" is provided so that existing code (MainActivity)
// works without any changes.
class HomeViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        fetchAllMovies()
    }

    private fun fetchAllMovies() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // Execute API calls concurrently for better performance
            val nowPlayingDeferred = async { repository.getNowPlayingMovies(1) }
            val popularDeferred = async { repository.getPopularMovies(1) }
            val topRatedDeferred = async { repository.getTopRatedMovies(1) }
            val upcomingDeferred = async { repository.getUpcomingMovies(1) }

            // Await all results
            val nowPlayingResult = nowPlayingDeferred.await()
            val popularResult = popularDeferred.await()
            val topRatedResult = topRatedDeferred.await()
            val upcomingResult = upcomingDeferred.await()

            // Update state with results
            _state.value = _state.value.copy(
                isLoading = false,
                nowPlayingMovies = extractData(nowPlayingResult),
                popularMovies = extractData(popularResult),
                topRatedMovies = extractData(topRatedResult),
                upcomingMovies = extractData(upcomingResult),

                // Capture the first error if any occurs across requests
                error = listOf(nowPlayingResult, popularResult, topRatedResult, upcomingResult)
                    .firstOrNull { it is Resource.Error }?.message
            )
        }
    }

    // Helper to extract data from Resource wrapper
    private fun extractData(resource: Resource<*>): List<Movie> {
        return if (resource is Resource.Success) {
            (resource.data as? com.yasinguzel.movieapp.data.model.MovieResponse)?.results ?: emptyList()
        } else {
            emptyList()
        }
    }
}