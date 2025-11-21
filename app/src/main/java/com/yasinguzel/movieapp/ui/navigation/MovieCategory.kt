package com.yasinguzel.movieapp.ui.navigation

/**
 * Defines the types of movie lists available in the app.
 * Used to navigate to the correct "Show All" list.
 */
enum class MovieCategory(val key: String) {
    NOW_PLAYING("now_playing"),
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    UPCOMING("upcoming")
}