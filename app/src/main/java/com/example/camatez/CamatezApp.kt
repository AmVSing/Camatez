package com.example.camatez

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.camatez.data.settings.SettingsRepository
import com.example.camatez.domain.Settings
import com.example.camatez.ui.game.GameScreen
import com.example.camatez.ui.game.GameViewModel
import com.example.camatez.ui.loading.LoadingScreen
import com.example.camatez.ui.menu.MainMenuScreen
import com.example.camatez.ui.scores.HighScoresScreen
import com.example.camatez.ui.scores.HighScoresViewModel
import com.example.camatez.ui.setup.SetupScreen
import com.example.camatez.ui.setup.SetupViewModel
import com.example.camatez.ui.theme.CamatezTheme
import kotlinx.coroutines.launch

@Composable
fun CamatezApp() {
    val context = LocalContext.current.applicationContext
    val settingsRepository = remember { SettingsRepository(context) }
    val scope = rememberCoroutineScope()

    var screen by remember { mutableStateOf<AppScreen>(AppScreen.MainMenu) }

    val settings by settingsRepository.settingsFlow.collectAsState(initial = null)

    CamatezTheme {
        val currentSettings = settings
        if (currentSettings == null) {
            LoadingScreen()
            return@CamatezTheme
        }

        when (screen) {
            AppScreen.MainMenu -> {
                MainMenuScreen(
                    onPlay = { screen = AppScreen.Game },
                    onConfig = { screen = AppScreen.Settings },
                    onHighScores = { screen = AppScreen.HighScores },
                )
            }

            AppScreen.Settings -> {
                val setupViewModel: SetupViewModel = viewModel()

                LaunchedEffect(screen, currentSettings) {
                    if (screen == AppScreen.Settings) {
                        setupViewModel.loadConfig(currentSettings)
                    }
                }

                SetupScreen(
                    viewModel = setupViewModel,
                    onSaveConfig = { newSettings ->
                        scope.launch {
                            settingsRepository.save(newSettings)
                        }
                        screen = AppScreen.MainMenu
                    },
                    onBackToMenu = {
                        screen = AppScreen.MainMenu
                    },
                )
            }

            AppScreen.HighScores -> {
                val highScoresViewModel: HighScoresViewModel = viewModel()
                HighScoresScreen(
                    viewModel = highScoresViewModel,
                    onBackToMenu = {
                        screen = AppScreen.MainMenu
                    },
                )
            }

            AppScreen.Game -> {
                val gameViewModel: GameViewModel = viewModel()
                GameScreen(
                    viewModel = gameViewModel,
                    settings = currentSettings,
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