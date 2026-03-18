package com.example.camatez

sealed interface AppScreen {
    data object MainMenu : AppScreen

    data object Settings : AppScreen

    data object Game : AppScreen

    data object HighScores : AppScreen
}
