package com.example.camatez.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CamatezColorScheme =
    darkColorScheme(
        background = AppBackground,
        surface = AppSurface,
        surfaceVariant = AppSurfaceAlt,
        primary = AppAccent,
        onBackground = AppText,
        onSurface = AppText,
        onSurfaceVariant = AppText,
        onPrimary = AppBackground,
    )

@Composable
fun CamatezTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CamatezColorScheme,
        content = content,
    )
}
