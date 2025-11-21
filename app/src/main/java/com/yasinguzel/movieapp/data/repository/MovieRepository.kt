package com.yasinguzel.movieapp.data.repository

import com.yasinguzel.movieapp.data.api.RetrofitClient
import com.yasinguzel.movieapp.data.model.MovieResponse
import com.yasinguzel.movieapp.util.Resource
import java.lang.Exception

class MovieRepository {

    private val api = RetrofitClient.api

    // Popular films
    suspend fun getPopularMovies(page: Int): Resource<MovieResponse> {
        return try {
            val response = api.getPopularMovies(page = page)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Bilinmeyen bir hata oluştu")
        }
    }

    // Now Playing
    suspend fun getNowPlayingMovies(page: Int): Resource<MovieResponse> {
        return try {
            val response = api.getNowPlayingMovies(page = page)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Hata")
        }
    }

    // Top Rated
    suspend fun getTopRatedMovies(page: Int): Resource<MovieResponse> {
        return try {
            val response = api.getTopRatedMovies(page = page)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Hata")
        }
    }

    // Upcoming
    suspend fun getUpcomingMovies(page: Int): Resource<MovieResponse> {
        return try {
            val response = api.getUpcomingMovies(page = page)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Hata")
        }
    }

    // Search
    suspend fun searchMovies(query: String, page: Int): Resource<MovieResponse> {
        return try {
            val response = api.searchMovies(query = query, page = page)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Hata")
        }
    }

    suspend fun getMovieDetail(movieId: Int): Resource<com.yasinguzel.movieapp.data.model.Movie> {
        return try {
            val response = api.getMovieDetail(movieId = movieId)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Hata oluştu")
        }
    }
}