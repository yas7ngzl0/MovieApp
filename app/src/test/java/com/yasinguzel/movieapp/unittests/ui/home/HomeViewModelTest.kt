package com.yasinguzel.movieapp.unittests.ui.home

import com.yasinguzel.movieapp.data.model.Movie
import com.yasinguzel.movieapp.data.model.MovieResponse
import com.yasinguzel.movieapp.data.repository.MovieRepository
import com.yasinguzel.movieapp.ui.home.HomeViewModel
import com.yasinguzel.movieapp.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    // 1. Create a fake (Mock) Repository
    private val mockRepository = mockk<MovieRepository>()

    // ViewModel to be tested (Not initialized yet)
    private lateinit var viewModel: HomeViewModel

    // Special Dispatcher for Coroutines tests to control time
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Replace Main Thread with test dispatcher (since there is no UI loop in unit tests)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        // Reset Main dispatcher after tests are done
        Dispatchers.resetMain()
    }

    @Test
    fun `when init fetchAllMovies success, state should show movies`() = runTest {
        // --- GIVEN (Preparation) ---

        // Create a fake movie list
        val fakeMovies = listOf(
            Movie(id = 1, title = "Fake Movie 1", overview = "", posterPath = "", backdropPath = "", releaseDate = "", voteAverage = 8.0, genreIds = null, genres = null),
            Movie(id = 2, title = "Fake Movie 2", overview = "", posterPath = "", backdropPath = "", releaseDate = "", voteAverage = 9.0, genreIds = null, genres = null)
        )
        val fakeResponse = MovieResponse(page = 1, results = fakeMovies, totalPages = 1, totalResults = 2)

        // Instruct Mock Repository: "If someone asks for any movie list, return this fake response"
        // NOTE: There are 4 different requests in ViewModel, we must mock them all
        coEvery { mockRepository.getNowPlayingMovies(1) } returns Resource.Success(fakeResponse)
        coEvery { mockRepository.getPopularMovies(1) } returns Resource.Success(fakeResponse)
        coEvery { mockRepository.getTopRatedMovies(1) } returns Resource.Success(fakeResponse)
        coEvery { mockRepository.getUpcomingMovies(1) } returns Resource.Success(fakeResponse)

        // --- WHEN (Action) ---

        // Initialize ViewModel (fetchAllMovies will run in the init block)
        // We inject the mockRepository via constructor
        viewModel = HomeViewModel(mockRepository)

        // Wait for Coroutines operations to finish (advance virtual time)
        testDispatcher.scheduler.advanceUntilIdle()

        // --- THEN (Verification) ---

        val currentState = viewModel.state.value

        // There should be no error
        assertEquals(null, currentState.error)
        // Loading should be finished
        assertFalse(currentState.isLoading)
        // Popular movies list should match our fake list
        assertEquals(2, currentState.popularMovies.size)
        assertEquals("Fake Movie 1", currentState.popularMovies[0].title)

        println("TEST SUCCESSFUL: ViewModel processed fake data correctly.")
    }

    @Test
    fun `when api returns error, state should show error message`() = runTest {
        // --- GIVEN ---
        val errorMessage = "No Internet Connection"

        // Configure Repository to return error for one call
        coEvery { mockRepository.getNowPlayingMovies(1) } returns Resource.Error(errorMessage)
        // Even if others succeed, our logic might show error based on requirements (or partial success)
        // For this test scenario, we simulate mixed responses or full failure
        coEvery { mockRepository.getPopularMovies(1) } returns Resource.Success(MovieResponse(1, emptyList(), 1, 0))
        coEvery { mockRepository.getTopRatedMovies(1) } returns Resource.Success(MovieResponse(1, emptyList(), 1, 0))
        coEvery { mockRepository.getUpcomingMovies(1) } returns Resource.Success(MovieResponse(1, emptyList(), 1, 0))

        // --- WHEN ---
        viewModel = HomeViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // --- THEN ---
        val currentState = viewModel.state.value

        // Error message should be populated
        assertEquals(errorMessage, currentState.error)
        assertFalse(currentState.isLoading)

        println("TEST SUCCESSFUL: ViewModel handled error state correctly.")
    }
}