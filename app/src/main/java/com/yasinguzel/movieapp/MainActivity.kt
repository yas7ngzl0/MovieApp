package com.yasinguzel.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.yasinguzel.movieapp.ui.theme.MovieAppTheme

import androidx.navigation.compose.rememberNavController
import com.yasinguzel.movieapp.ui.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables edge-to-edge (full screen) experience
        enableEdgeToEdge()

        setContent {
            // Apply our custom Theme
            MovieAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    // This will automatically load the 'startDestination' (HomeScreen)
                    NavGraph(navController = navController)
                }
            }
        }
    }
}