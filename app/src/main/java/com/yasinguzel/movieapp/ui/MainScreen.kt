package com.yasinguzel.movieapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yasinguzel.movieapp.R
import com.yasinguzel.movieapp.ui.navigation.NavGraph
import com.yasinguzel.movieapp.ui.navigation.Screen
import com.yasinguzel.movieapp.ui.theme.PrimaryRed

@Composable
fun MainScreen(navController: NavHostController) {
    // 1. Track current route to hide BottomBar on Detail/List screens
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show BottomBar ONLY on Home and Search screens
    val showBottomBar = currentRoute == Screen.Home.route || currentRoute == Screen.Search.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        // 2. The Container for Screens
        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            NavGraph(navController = navController)
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.Transparent // Or MaterialTheme.colorScheme.surface
    ) {
        // HOME TAB
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = { onNavigate(Screen.Home.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.home_tab)) },
            label = { Text(stringResource(R.string.home_tab)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryRed,
                selectedTextColor = PrimaryRed,
                indicatorColor = PrimaryRed.copy(alpha = 0.2f)
            )
        )

        // SEARCH TAB
        NavigationBarItem(
            selected = currentRoute == Screen.Search.route,
            onClick = { onNavigate(Screen.Search.route) },
            icon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search_tab)) },
            label = { Text(stringResource(R.string.search_tab)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryRed,
                selectedTextColor = PrimaryRed,
                indicatorColor = PrimaryRed.copy(alpha = 0.2f)
            )
        )
    }
}