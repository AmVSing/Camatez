package com.example.camatez

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.camatez.domain.Config
import com.example.camatez.domain.defaultConfig
import com.example.camatez.ui.game.GameScreen
import com.example.camatez.ui.game.GameViewModel
import com.example.camatez.ui.menu.MainMenuScreen
import com.example.camatez.ui.scores.HighScoresScreen
import com.example.camatez.ui.setup.SetupScreen
import com.example.camatez.ui.setup.SetupViewModel
import com.example.camatez.ui.theme.CamatezTheme

@Composable
fun CamatezApp() {
    var screen by remember { mutableStateOf<AppScreen>(AppScreen.MainMenu) }
    var currentConfig by remember { mutableStateOf<Config>(defaultConfig()) }

    CamatezTheme {
        when (screen) {
            AppScreen.MainMenu -> {
                MainMenuScreen(
                    onPlay = { screen = AppScreen.Game },
                    onConfig = { screen = AppScreen.Config },
                    onHighScores = { screen = AppScreen.HighScores },
                )
            }

            AppScreen.Config -> {
                val setupViewModel: SetupViewModel = viewModel()

                LaunchedEffect(currentConfig) {
                    setupViewModel.loadConfig(currentConfig)
                }

                SetupScreen(
                    viewModel = setupViewModel,
                    onSaveConfig = { config ->
                        currentConfig = config
                        screen = AppScreen.MainMenu
                    },
                    onBackToMenu = {
                        screen = AppScreen.MainMenu
                    },
                )
            }

            AppScreen.HighScores -> {
                HighScoresScreen(
                    onBackToMenu = {
                        screen = AppScreen.MainMenu
                    },
                )
            }

            AppScreen.Game -> {
                val gameViewModel: GameViewModel = viewModel()
                GameScreen(
                    viewModel = gameViewModel,
                    config = currentConfig,
                    onQuitToMenu = {
                        screen = AppScreen.MainMenu
                    },
                    onGoToMenuAfterGame = {
                        screen = AppScreen.MainMenu
                    },
                )
            }
        }
    }
}