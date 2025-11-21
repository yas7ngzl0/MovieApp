package com.yasinguzel.movieapp.data.api

import com.yasinguzel.movieapp.data.model.Movie
import com.yasinguzel.movieapp.data.model.MovieResponse
import com.yasinguzel.movieapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    // Now Playing
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int = 1
    ): MovieResponse

    // Popular
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int = 1
    ): MovieResponse

    // Top Rated
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int = 1
    ): MovieResponse

    // Upcoming
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int = 1
    ): MovieResponse


    // Search Feature
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Movie
}