package com.yasinguzel.movieapp.ui.search

import android.app.Application
import com.yasinguzel.movieapp.data.model.Movie
import com.yasinguzel.movieapp.data.model.MovieResponse
import com.yasinguzel.movieapp.data.repository.MovieRepository
import com.yasinguzel.movieapp.util.Resource
import com.yasinguzel.movieapp.util.SearchHistoryManager
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    // Mocks
    private val mockRepository = mockk<MovieRepository>(relaxed = true)
    private val mockHistoryManager = mockk<SearchHistoryManager>(relaxed = true)
    private val mockApplication = mockk<Application>()

    // Subject under test
    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load search history`() = runTest {
        // --- GIVEN ---
        val fakeHistory = listOf("Matrix", "Batman", "Inception")
        // When getHistory is called, return our fake list
        every { mockHistoryManager.getHistory() } returns fakeHistory

        // --- WHEN ---
        // Initialize ViewModel with mocks
        viewModel = SearchViewModel(mockApplication, mockRepository, mockHistoryManager)

        // --- THEN ---
        assertEquals(fakeHistory, viewModel.state.value.history)
        println("TEST SUCCESSFUL: History loaded on init.")
    }

    @Test
    fun `onQueryChange should wait for debounce before searching`() = runTest {
        // --- GIVEN ---
        val query = "Avatar"
        // Mock repository to return empty success so we don't crash
        coEvery { mockRepository.searchMovies(any(), any()) } returns Resource.Success(
            MovieResponse(1, emptyList(), 1, 0)
        )

        viewModel = SearchViewModel(mockApplication, mockRepository, mockHistoryManager)

        // --- WHEN ---
        viewModel.onQueryChange(query)

        // --- THEN (Check Immediate State) ---
        // Immediately after typing, query should be updated but loading should be FALSE (waiting for delay)
        assertEquals(query, viewModel.state.value.query)
        assertFalse(viewModel.state.value.isLoading)
        println("CHECK 1: Query updated, but search hasn't started yet (Debounce working).")

        // --- WHEN (Fast Forward Time) ---
        // Advance time by 500ms (Halfway) - Should still be waiting
        testDispatcher.scheduler.advanceTimeBy(500)
        assertFalse(viewModel.state.value.isLoading)

        // Advance time by another 501ms (Total > 1000ms) - Should trigger search
        testDispatcher.scheduler.advanceTimeBy(501)

        // Let coroutines finish
        testDispatcher.scheduler.runCurrent()

        // Verify that searchMovies was actually called
        verify {
            // "verify" checks if a method on a mock object was called
            mockHistoryManager.addSearchQuery(query)
        }

        println("TEST SUCCESSFUL: Search triggered after 1000ms delay.")
    }

    @Test
    fun `performSearch success should update movies list`() = runTest {
        // --- GIVEN ---
        val query = "Joker"
        val fakeMovies = listOf(
            Movie(id = 1, title = "Joker", overview = "", posterPath = null, backdropPath = null, releaseDate = null, voteAverage = 8.0, genreIds = null, genres = null)
        )
        val successResponse = Resource.Success(MovieResponse(1, fakeMovies, 1, 1))

        coEvery { mockRepository.searchMovies(query, 1) } returns successResponse

        viewModel = SearchViewModel(mockApplication, mockRepository, mockHistoryManager)

        // --- WHEN ---
        viewModel.onQueryChange(query)

        // Fast forward past the debounce delay
        testDispatcher.scheduler.advanceUntilIdle()

        // --- THEN ---
        val currentState = viewModel.state.value
        assertFalse(currentState.isLoading)
        assertEquals(1, currentState.movies.size)
        assertEquals("Joker", currentState.movies.first().title)

        println("TEST SUCCESSFUL: Movies list updated after search.")
    }
}