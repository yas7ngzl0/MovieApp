package com.yasinguzel.movieapp.data.model

import com.google.gson.annotations.SerializedName

// For list structure
data class MovieResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<Movie>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

//  For every single movie object
data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?, //  Poster
@SerializedName("backdrop_path") val backdropPath: String?,
@SerializedName("release_date") val releaseDate: String?,
@SerializedName("vote_average") val voteAverage: Double,
@SerializedName("genre_ids") val genreIds: List<Int>?
)