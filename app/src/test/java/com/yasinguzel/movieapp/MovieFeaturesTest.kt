package com.yasinguzel.movieapp

import com.yasinguzel.movieapp.data.repository.MovieRepository
import com.yasinguzel.movieapp.util.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieFeaturesTest {

    private val repository = MovieRepository()

    @Test
    fun testPaginationLogic() = runBlocking {
        println("TEST: Testing Infinite Scroll Logic (Pagination)...")

        // Step 1: Fetch the first page
        println("STEP 1: Fetching Page 1...")
        val page1Result = repository.getPopularMovies(page = 1)
        val list1 = page1Result.data?.results

        // Check 1: List should not be empty
        assertTrue("Page 1 should not be empty", !list1.isNullOrEmpty())
        val firstMovieTitle = list1?.first()?.title
        println("   -> Page 1 First Movie: $firstMovieTitle")

        // Step 2: Fetch the second page (Infinite scrolling scenario)
        println("STEP 2: Fetching Page 2...")
        val page2Result = repository.getPopularMovies(page = 2)
        val list2 = page2Result.data?.results

        // Check 2: Second list should not be empty
        assertTrue("Page 2 should not be empty", !list2.isNullOrEmpty())
        val secondPageFirstMovie = list2?.first()?.title
        println("   -> Page 2 First Movie: $secondPageFirstMovie")

        // CRITICAL CHECK: First movies of Page 1 and Page 2 must be different.
        // If they are the same, the API is returning the same page (Pagination is broken).
        assertNotEquals("Pagination failed! Page 1 and Page 2 are same.", firstMovieTitle, secondPageFirstMovie)

        println("TEST: PAGINATION SUCCESS! Distinct data received.")
    }

    @Test
    fun testMovieDetailLogic() = runBlocking {
        println("TEST: Testing Detail Screen Logic...")

        // A known movie ID for testing (e.g., 550 - Fight Club or an ID from the previous list).
        // To reduce risk, let's get an ID from the popular list first.
        val popularMovie = repository.getPopularMovies(1).data?.results?.first()
        val testId = popularMovie?.id ?: 550 // If list is empty, try 550 (Fight Club)

        println("STEP 1: Fetching details for Movie ID: $testId")

        val detailResult = repository.getMovieDetail(testId)

        when(detailResult) {
            is Resource.Success -> {
                val movie = detailResult.data
                println("   -> Movie Title: ${movie?.title}")
                println("   -> Movie Overview: ${movie?.overview?.take(50)}...")

                // Assertions
                assertNotNull("Movie data should not be null", movie)
                assertTrue("Title should not be empty", movie?.title?.isNotEmpty() == true)
                assertTrue("Overview should not be empty", movie?.overview?.isNotEmpty() == true)

                println("TEST: DETAIL FETCH SUCCESS!")
            }
            is Resource.Error -> {
                println("TEST: ERROR! ${detailResult.message}")
                assert(false) // Fail the test
            }
            else -> println("Loading...")
        }
    }
}