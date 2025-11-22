package com.yasinguzel.movieapp.integrationTests

import com.yasinguzel.movieapp.data.repository.MovieRepository
import com.yasinguzel.movieapp.util.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Simple Unit Test to verify API connection.
 */
class MovieRepositoryTest {

    @Test
    fun testApiConnection() = runBlocking {
        // Create repository instance
        val repository = MovieRepository()
        println("TEST: Requesting data...")

        // Make actual API request (Used for smoke testing)
        val result = repository.getPopularMovies(1)

        when(result) {
            is Resource.Success -> {
                val movies = result.data?.results
                println("TEST: SUCCESS! Movie Count: ${movies?.size}")
                // Condition for test to pass
                assert(!movies.isNullOrEmpty())
            }
            is Resource.Error -> {
                println("TEST: ERROR! ${result.message}")
                // Fail the test
                assert(false)
            }
            else -> println("Loading...")
        }
    }
}