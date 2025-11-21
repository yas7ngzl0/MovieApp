package com.yasinguzel.movieapp.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Search : Screen("search_screen")
    object Detail : Screen("detail_screen/{movieId}") {
        // Helper to construct route with arguments
        fun createRoute(movieId: Int) = "detail_screen/$movieId"
    }
}