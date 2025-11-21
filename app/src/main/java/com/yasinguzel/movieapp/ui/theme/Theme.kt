package com.yasinguzel.movieapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark Theme Scheme
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryRed,
    secondary = SecondaryBlue,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkOnBackground,
    onSurface = DarkOnBackground,
    error = ErrorColor
)

// Light Theme Scheme
private val LightColorScheme = lightColorScheme(
    primary = PrimaryRed,
    secondary = SecondaryBlue,
    background = LightBackground,
    surface = LightSurface,
    onBackground = LightOnBackground,
    onSurface = LightOnBackground,
    error = ErrorColor
)

@Composable
fun MovieAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Automatically detect system theme
    // Dynamic color (Android 12+) is available on supported devices
    dynamicColor: Boolean = false, // Disable dynamic color to use our custom color palette
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Configure Status Bar appearance
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Android now enforces Edge-to-Edge, making the status bar transparent by default.
            // We only need to manage the contrast of the icons (Light or Dark).

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Uses Type.kt if available
        content = content
    )
}