package com.yasinguzel.movieapp.util

import com.yasinguzel.movieapp.BuildConfig //import build config to get api key

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"


    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original/"


    const val API_KEY = BuildConfig.TMDB_API_KEY

    // Dummy Profile Image URL (Placeholder)
    const val DUMMY_AVATAR = "https://i.pravatar.cc/300"
}