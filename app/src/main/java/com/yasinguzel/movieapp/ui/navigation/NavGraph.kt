package com.yasinguzel.movieapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yasinguzel.movieapp.R
import com.yasinguzel.movieapp.ui.home.HomeScreen
import com.yasinguzel.movieapp.ui.movielist.MovieListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // 1. Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onMovieClick = { movieId ->
                    // Navigate to Detail (Later)
                    // navController.navigate(Screen.Detail.createRoute(movieId))
                },
                onShowAllClick = { category ->
                    // Navigate to List Screen with Category Argument
                    navController.navigate(Screen.MovieList.createRoute(category))
                }
            )
        }

        // 2. Movie List Screen (Show All)
        composable(
            route = Screen.MovieList.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            // Get the category string from arguments
            val categoryKey = backStackEntry.arguments?.getString("category") ?: MovieCategory.POPULAR.key

            // Helper to convert key to User Friendly Title (e.g. "popular" -> "Popular")
            val title = when(categoryKey) {
                MovieCategory.NOW_PLAYING.key -> stringResource(R.string.now_playing)
                MovieCategory.POPULAR.key -> stringResource(R.string.popular)
                MovieCategory.TOP_RATED.key -> stringResource(R.string.top_rated)
                MovieCategory.UPCOMING.key -> stringResource(R.string.upcoming)
                else -> stringResource(R.string.app_name)
            }

            MovieListScreen(
                onMovieClick = { movieId ->
                    // Navigate to Detail (Later)
                    // navController.navigate(Screen.Detail.createRoute(movieId))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                categoryTitle = title
            )
        }

        // 3. Detail Screen (Coming Soon)
    }
}